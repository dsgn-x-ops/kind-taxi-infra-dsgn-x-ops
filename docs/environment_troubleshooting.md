# Environment Troubleshooting Log

This document outlines the main issues encountered while setting up and running the `kind-taxi-infra-dsgn-x-ops` project, including environment-specific bugs, workarounds and lessons learned.

---

## Issue 1 – WSL + Gradle Project Sync Failure

**Symptoms:**
- Gradle sync fails with:
``Cannot invoke "java.io.File.getPath()" because the return value of "org.gradle.tooling.internal.consumer.parameters.ConsumerOperationParameters.getJavaHome()" is nul``
- IntelliJ fails to resolve Gradle modules and shows "Nothing to show" in the Gradle panel.

**Cause:**
- Known [Gradle integration bug in IntelliJ on WSL environments](https://youtrack.jetbrains.com/issue/IDEA-367587/Intellij-installed-in-windows-cant-build-projects-in-WSL).

**Resolution:**
- Moved the project to a **Windows-native path** (e.g., `C:\Users\[User]\Documents\...`) instead of using the WSL Linux file system.
- IntelliJ was able to detect Gradle correctly after moving the project.

---

## Issue 2 – SSH Key Access Denied (GitHub)

**Symptoms:**
- Running `git clone` from PowerShell results in:
``Permission denied (publickey)``

**Steps Taken:**
1. Verified SSH agent status:
 ```powershell
 Get-Service -Name ssh-agent | Set-Service -StartupType Manual
 Start-Service ssh-agent
 ```
2. Added the key: 
```powershell
ssh-add C:\Users\[User]\.ssh\github
 ```
3. Created ~/.ssh/config with:
```powershell
Host github.com
  HostName github.com
  User git
  IdentityFile C:/Users/[User]/.ssh/github
  IdentitiesOnly yes
 ```
4. Tested:
```powershell
ssh -T git@github.com
# → "Hi username! You've successfully authenticated..."
```
---

## Issue 3 – Java 24 is not compatible with Gradle 8.5

**Symptoms:**

- IntelliJ shows:
``Your build is currently configured to use incompatible Java 24.0.1 and Gradle 8.5.``

**Resolution:**
- Installed OpenJDK 17 (Temurin)
- Confirmed installation:
```powershell
java -version
javac -version
```
- In IntelliJ:
  - Go to File > Project Structure > Project
  - Click "Add JDK from Disk"
  - Select JDK 17 folder (e.g., C:\Program Files\Eclipse Adoptium\jdk-17.x.x)
  - Set it as the SDK for the project.