# fish-shop-slack-bot


[![Build Status](https://travis-ci.com/jakub-tucek/fish-shop-slack-bot.svg?branch=master)](https://travis-ci.com/jakub-tucek/fish-shop-slack-bot)

Slack bot that automates order process from restaurant rybarna.net

## Start from release

1. Download realease zip file
2. unzip [filename]
3. Define environment variable (shown below)
4. Go to unzipped directory and run /bin/fish-shop-slack-bot

## Start up

1. Set env variables 
    ```
    export FISH_SHOP_BOT_MESSAGE_URL="https://hooks.slack.com/services/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"
    export FISH_SHOP_BOT_VERIFICATION_TOKEN="123"
    export FISH_SHOP_APPLICATION_SECRET="123123123123123"
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

1. **/fish-order** [0-5]* - orders meal for user who enterd command, numbers delivered by space
2. **/fish-menu** - creates menu
2. **/fish-reset** - reset orders of person who entered command
2. **/fish-complete** - create order on fish shop, credentials are loaded from configuration file

## More configuration

Remaining configuration is set in conf/application.conf.
There username/mail/phone number can be modified.