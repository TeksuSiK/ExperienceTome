# ExperienceTome

## Overview
ExperienceTome is plugin inspired by XP Tome mod for Forge/Fabric. It introduces new single item - Experience Tome. With this tome you can store and retrieve XP. By default it can store up to 30 levels (1395 XP). 

## Default recipe:
![Default recipe](https://i.imgur.com/Cnpd6C3.png)

## Usage
Sneak + Right-click - Deposit experience to tome
Right-click - Withdraw experience from tome

## Installation
Simply drop [downloaded .jar](https://github.com/TeksuSiK/ExperienceTome/releases/latest) file to plugins directory in your server's root and start your server. 

## Configuration
Plugin create a directory with config.yml file, where you can customize maximum experience that can be stored in a single book, system messages, tome material, display name, lore and tome crafting.

## For Developers
You can listen for `ExperienceDepositEvent` and `ExperienceWithdrawEvent` events in your plugin. 
### Maven
Add repository to the `repositories` section:
```xml
<repository>
  <id>teksusik-releases</id>
  <url>https://repo.teksusik.pl/releases</url>
</repository>
```
Add dependency to the `dependencies` section:
```xml
<dependency>
  <groupId>pl.teksusik</groupId>
  <artifactId>ExperienceTome</artifactId>
  <version>1.1.0</version>
</dependency>
```
### Gradle (Kotlin Script)
Add repository to the `repositories` section:
```kotlin
maven(url = "https://repo.teksusik.pl/releases")
```
Add dependency to the `dependencies` section:
```kotlin
implementation("pl.teksusik:ExperienceTome:1.1.0")
```

## Support
If you are experiencing any troubles with using ExperienceTome plugin open a new issue on ExperienceTome's GitHub.
