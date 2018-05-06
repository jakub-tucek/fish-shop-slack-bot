# fish-shop-slack-bot


[![Build Status](https://travis-ci.com/jakub-tucek/fish-shop-slack-bot.svg?branch=master)](https://travis-ci.com/jakub-tucek/fish-shop-slack-bot)

Slack bot that automates order process from restaurant rybarna.net

## Start up release

1. Download release zip file
2. unzip downloaded file
3. Define environment variables (shown below)
4. Go to unzipped directory and run **/bin/fish-shop-slack-bot** script

## Start up development

1. Set env variables 
    ```
    export FISH_SHOP_BOT_VERIFICATION_TOKEN="XXXXXXXXXXX"
    export FISH_SHOP_APPLICATION_SECRET="XXXXXXXXXXXXX"
    ```
2. Run sbt run
    ```
    sbt run
    ```
3. Profit

## App secret generation

```
sbt playGenerateSecret
```

## Usage

1. **/fish-order** [0-5]* - orders meal for user who entered command, numbers are delivered by space
2. **/fish-menu** - creates menu
2. **/fish-reset** - reset orders of person who entered command
2. **/fish-complete** - create order on fish shop, user details are loaded from configuration file

## More configuration

Remaining configuration is set in conf/application.conf.