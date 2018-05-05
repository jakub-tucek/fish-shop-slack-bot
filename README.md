# fish-shop-slack-bot


[![Build Status](https://travis-ci.com/jakub-tucek/fish-shop-slack-bot.svg?branch=master)](https://travis-ci.com/jakub-tucek/fish-shop-slack-bot)

Slack bot that automates order process from restaurant rybarna.net


## Start up

1. Set env variables 
    ```
    export FISH_SHOP_BOT_MESSAGE_URL="https://hooks.slack.com/services/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"
    export FISH_SHOP_BOT_VERIFICATION_TOKEN="123"
    ```
2. Run sbt run
    ```
    sbt run
    ```
3. Profit


## Usage

1. **/fish-order** [0-5]* - orders meal for user who enterd command, numbers delivered by space
2. **/fish-menu** - creates menu
2. **/fish-reset** - reset orders of person who entered command
2. **/fish-complete** - create order on fish shop, credentials are loaded from configuration file