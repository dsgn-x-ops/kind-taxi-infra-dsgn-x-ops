import pytest
from datetime import datetime
from unittest.mock import Mock, patch
import pika
from generator import TaxiDataGenerator

@pytest.fixture
def generator():
    with patch('pika.BlockingConnection'):
        gen = TaxiDataGenerator()
        gen.channel = Mock()
        return gen

def test_generate_random_date(generator):
    date = generator.generate_random_date()
    assert isinstance(date, datetime)
    assert datetime(2024, 1, 1) <= date <= datetime(2024, 12, 31)

def test_calculate_price(generator):
    start_location = {
        "latitude": 40.7128,
        "longitude": -74.0060,
        "place": "New York City"
    }
    end_location = {
        "latitude": 40.7580,
        "longitude": -73.9855,
        "place": "Times Square"
    }
    price = generator.calculate_price(start_location, end_location)
    assert isinstance(price, float)
    assert price > 0

def test_generate_entry(generator):
    entry = generator.generate_entry()
    assert isinstance(entry, dict)
    assert "start" in entry
    assert "end" in entry
    assert "important_places" in entry
    assert "start_date" in entry
    assert "end_date" in entry
    assert "price" in entry
    assert len(entry["important_places"]) <= 3

def test_rabbitmq_connection_error():
    with patch('pika.BlockingConnection') as mock_connection:
        mock_connection.side_effect = pika.exceptions.AMQPConnectionError()
        with pytest.raises(Exception) as exc_info:
            TaxiDataGenerator()
        assert "Failed to connect to RabbitMQ after 5 attempts" in str(exc_info.value)

@pytest.mark.asyncio
async def test_generate_and_send_batch(generator):
    generator.generate_and_send_batch()
    assert generator.channel.basic_publish.called

    # Get the last call arguments
    args = generator.channel.basic_publish.call_args[1]
    assert args['exchange'] == 'taxi_data'
    assert args['routing_key'] == 'ride'
    assert 'body' in args
    assert isinstance(args['body'], str)

def test_generate_unique_locations(generator):
    entry = generator.generate_entry()
    assert entry["start"]["place"] != entry["end"]["place"]
    for place in entry["important_places"]:
        assert place["place"] not in [entry["start"]["place"], entry["end"]["place"]]

def test_valid_date_range(generator):
    entry = generator.generate_entry()
    start_date = datetime.strptime(entry["start_date"], "%Y-%m-%d %H:%M:%S")
    end_date = datetime.strptime(entry["end_date"], "%Y-%m-%d %H:%M:%S")
    assert start_date <= end_date
    assert end_date - start_date <= timedelta(hours=2)

if __name__ == '__main__':
    pytest.main(['-v'])