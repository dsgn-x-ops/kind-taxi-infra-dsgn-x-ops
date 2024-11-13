import json
import random
import logging
import os
import time
import schedule
import pika
from datetime import datetime, timedelta
from geopy.distance import geodesic
from pythonjsonlogger import jsonlogger

# Configure JSON logging
logger = logging.getLogger()
logHandler = logging.StreamHandler()
formatter = jsonlogger.JsonFormatter(
    fmt='%(asctime)s %(levelname)s %(name)s %(message)s'
)
logHandler.setFormatter(formatter)
logger.addHandler(logHandler)
logger.setLevel(logging.INFO)

# Constants
PLACES = {
    "New York City": (40.7128, -74.0060),
    "Times Square": (40.7580, -73.9855),
    "Empire State Building": (40.748817, -73.985428),
    "Central Park": (40.7851, -73.9683),
    "Brooklyn Bridge": (40.7061, -73.9969),
    "Statue of Liberty": (40.6892, -74.0445),
    "Grand Central Terminal": (40.7527, -73.9772),
    "One World Trade Center": (40.7126, -74.0133),
    "Rockefeller Center": (40.7587, -73.9787),
    "The Metropolitan Museum of Art": (40.7794, -73.9632)
}

RATE_PER_KM = 2.5
BATCH_SIZE = int(os.getenv('BATCH_SIZE', '100'))
RABBITMQ_HOST = os.getenv('RABBITMQ_HOST', 'localhost')
RABBITMQ_USER = os.getenv('RABBITMQ_USER', 'guest')
RABBITMQ_PASS = os.getenv('RABBITMQ_PASS', 'guest')

class TaxiDataGenerator:
    def __init__(self):
        self.connection = None
        self.channel = None
        self.connect_to_rabbitmq()

    def connect_to_rabbitmq(self):
        retry_count = 0
        while retry_count < 5:
            try:
                credentials = pika.PlainCredentials(RABBITMQ_USER, RABBITMQ_PASS)
                parameters = pika.ConnectionParameters(
                    host=RABBITMQ_HOST,
                    credentials=credentials,
                    heartbeat=600,
                    connection_attempts=3
                )
                self.connection = pika.BlockingConnection(parameters)
                self.channel = self.connection.channel()

                # Declare exchange and queue
                self.channel.exchange_declare(
                    exchange='taxi_data',
                    exchange_type='direct',
                    durable=True
                )

                self.channel.queue_declare(
                    queue='taxi_rides',
                    durable=True
                )

                self.channel.queue_bind(
                    exchange='taxi_data',
                    queue='taxi_rides',
                    routing_key='ride'
                )

                logger.info("Successfully connected to RabbitMQ")
                break

            except pika.exceptions.AMQPConnectionError as error:
                retry_count += 1
                logger.error(f"Failed to connect to RabbitMQ (attempt {retry_count}): {error}")
                time.sleep(5)

        if retry_count == 5:
            raise Exception("Failed to connect to RabbitMQ after 5 attempts")

    def generate_random_date(self):
        start_date = datetime(2024, 1, 1)
        end_date = datetime(2024, 12, 31)
        days_between = (end_date - start_date).days
        random_days = random.randint(0, days_between)
        return start_date + timedelta(days=random_days)

    def calculate_price(self, start_location, end_location):
        distance = geodesic(
            (start_location["latitude"], start_location["longitude"]),
            (end_location["latitude"], end_location["longitude"])
        ).kilometers
        return round(distance * RATE_PER_KM, 2)

    def generate_entry(self):
        start_place = random.choice(list(PLACES.keys()))
        end_place = random.choice(list(PLACES.keys()))
        while end_place == start_place:
            end_place = random.choice(list(PLACES.keys()))

        important_places = random.sample(
            [p for p in PLACES.keys() if p not in [start_place, end_place]],
            min(3, len(PLACES) - 2)
        )

        start_date = self.generate_random_date()
        end_date = start_date + timedelta(minutes=random.randint(15, 120))

        start_location = {
            "latitude": PLACES[start_place][0],
            "longitude": PLACES[start_place][1],
            "place": start_place
        }

        end_location = {
            "latitude": PLACES[end_place][0],
            "longitude": PLACES[end_place][1],
            "place": end_place
        }

        important_locations = [
            {
                "latitude": PLACES[place][0],
                "longitude": PLACES[place][1],
                "place": place
            } for place in important_places
        ]

        price = self.calculate_price(start_location, end_location)

        return {
            "start": start_location,
            "end": end_location,
            "important_places": important_locations,
            "start_date": start_date.strftime("%Y-%m-%d %H:%M:%S"),
            "end_date": end_date.strftime("%Y-%m-%d %H:%M:%S"),
            "price": price
        }

    def generate_and_send_batch(self):
        try:
            entries = [self.generate_entry() for _ in range(BATCH_SIZE)]

            for entry in entries:
                self.channel.basic_publish(
                    exchange='taxi_data',
                    routing_key='ride',
                    body=json.dumps(entry),
                    properties=pika.BasicProperties(
                        delivery_mode=2,
                        content_type='application/json'
                    )
                )

            logger.info(f"Successfully generated and sent {BATCH_SIZE} entries")

        except (pika.exceptions.AMQPConnectionError,
                pika.exceptions.AMQPChannelError) as e:
            logger.error(f"RabbitMQ connection error: {str(e)}")
            self.connect_to_rabbitmq()
        except Exception as e:
            logger.error(f"Error generating and sending data: {str(e)}")

def main():
    # Adiciona delay inicial para dar tempo do RabbitMQ iniciar
    logger.info("Waiting for RabbitMQ to be ready...")
    time.sleep(30)

    generator = TaxiDataGenerator()

    # Schedule batch generation every 5 minutes
    schedule.every(1).minutes.do(generator.generate_and_send_batch)

    # Generate initial batch
    generator.generate_and_send_batch()

    while True:
        try:
            schedule.run_pending()
            time.sleep(1)
        except KeyboardInterrupt:
            logger.info("Shutting down generator...")
            if generator.connection and not generator.connection.is_closed:
                generator.connection.close()
            break
        except Exception as e:
            logger.error(f"Unexpected error: {str(e)}")
            time.sleep(5)

if __name__ == "__main__":
    main()