# Qualize

## Inspiration

After reading so much about Web 3.0, we began to wonder what we might do to help shape this new era in technology in our own backyard. Qualize, a decentralised blockchain system, was the end outcome of a brainstorming session that followed.
In the last several months, **Chainlink and its native DeFi ecosystem have flourished**, and we've been amazed not only by its great technical possibilities but also by the wonderful community and talent growing across all Chainlink verticals. Our team sees this as a **unique opportunity to make it easier for friends and family to share expenses from throughout the Chainlink ecosystem** and make them available in a user-friendly and accessible interface.


## What it does

Chainlink powers Qualize, a decentralised system that makes it easier for family and friends to pool their resources for common needs. Your pooled spending, receivables, and payables may all be found in one place, making it easier to determine who owes what. Qualize makes it simple to divide a group vacation, share a rental with roommates, or pay someone back after lunch. All of our transactions are fast, digital, secure, and worldwide, so we don't have to worry about losing track of our records. If you have an iPhone, an Android phone, or a computer, you can use our cloud-based platform.
Using our interactive dashboard, you may go down and filter data in order to get a distinct viewpoint. Using interactive drilled-down charts, the dashboard presents the amount you owe and the amount you owe your friend in a concise and clear manner, enabling data-driven organisational decisions to be made. The visual impact of our presentation captivates the audience.

![Qualize](https://user-images.githubusercontent.com/20425788/172657810-2a21c7bd-1678-4779-8907-7ce2ff2b721b.jpg)


On the account screen, you may see the total net transaction value for all of your friends, family, and others. You'll notice the account's current condition much faster thanks to the clever indication. You can use the red and green indicators to indicate the accounts in which you owe money to friends and family members, respectively. A link on the same page allows you to quickly settle an account expense in bitcoin in the currency of your friend or family member's choice.. We are able to maintain our records without worry of data loss because these transactions are driven by Chainlink, which is a digital, safe, and worldwide platform.

![Dashboard](https://user-images.githubusercontent.com/20425788/171668714-993b45c8-1bd9-40bf-a320-9de4560501d9.png)

We'll know for sure after the settlement transaction. After signing into Qualize, Ravi navigates to the Accounts page. The account must be paid in full if the red signal appears. Ravi decides to pay off his debt to David and launches a settlement transaction with David's permission. Complete the form, select your crypto currency of choice, and the system automatically determines its value. The transaction is started by connecting with the Chainlink API, which is done by Ravi. Before the transaction, there is a picture of the account on the screen. Bitcoin transactions are selected and completed for transmission via the Chainlink API. Cryptocurrency is used in the settlement. There are no outstanding balances in Ravi's account with Darren following the settlement.

![Database Design](https://user-images.githubusercontent.com/20425788/171668792-1312fcad-9941-4b65-b0a7-f87e6a345220.png)


The following are a few of the solution's most notable features. Our service is one of a kind because it allows you to pay your bills with your favourite cryptocurrency. Each day, currency conversion rate feeds are updated via a batch procedure that runs at the end of the working day. When your friend's account reaches a certain threshold, the smart threshold tool will notify you and remind you to take care of your own. Your friends, family members, or coworkers can use our platform to set up automatic payments and keep track of any events that take place.
Among the many other features that make our platform the most up-to-date, efficient, and safe solution available are offline access and expense import.


## How we built it

The Chainlink NodeJS SDK was used to connect to the blockchain. To build APIs, we turned to NodeJS. QuickBase (a low-code, no-code platform) was used to build the front end, which was then integrated into the platform.

## Challenges we ran into

We encountered a slew of roadblocks when building the platform. Because the technology we used was new, we had difficulties in determining the origin of our problems.

## Accomplishments that we're proud of

Full-stack Web 3.0 application built from the ground up. A good, user-friendly product that shows the synergy between design and engineering is something we strive to achieve with our designs that adhere to UI/UX guidelines.

## What we learned

It was interesting to hear about the various technologies, as well as the benefits and drawbacks of the current state of technology, and how blockchain and decentralised internet could help to alleviate some of these problems. Many new technologies have been introduced to us this year, including Chainlink, Smart Contracts, and Web3.

## What's next for Qualize

Integration of Google Home and Alexa with WhatsApp and Twilio Using machine learning, develop a model that can anticipate spending patterns and provide insights into those patterns..

## Project Structure

Node is required for generation and recommended for development. `package.json` is always generated for a better development experience with prettier, commit hooks, scripts and so on.

In the project root, Qualize generates configuration files for tools like git, prettier, eslint, husk, and others that are well known and you can find references in the web.

`/src/*` structure follows default Java structure.


- `.yo-resolve` (optional) - Yeoman conflict resolver
  Allows to use a specific action when conflicts are found skipping prompts for files that matches a pattern. Each line should match `[pattern] [action]` with pattern been a [Minimatch](https://github.com/isaacs/minimatch#minimatch) pattern and action been one of skip (default if ommited) or force. Lines starting with `#` are considered comments and are ignored.

- `/src/main/docker` - Docker configurations for the application and services that the application depends on

## Development

To start your application in the dev profile, run:

```
./gradlew
```


### Qualize Control Center

Qualize Control Center can help you manage and control your application(s). You can start a local control center server (accessible on http://localhost:7419) with:

```
docker-compose -f src/main/docker/qualize-control-center.yml up
```

## Building for production

### Packaging as jar

To build the final jar and optimize the qualize application for production, run:

```
./gradlew -Pprod clean bootJar
```

To ensure everything worked, run:

```
java -jar build/libs/*.jar
```

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```
./gradlew -Pprod -Pwar clean bootWar
```

## Testing

To launch your application's tests, run:

```
./gradlew test integrationTest jacocoTestReport
```


### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

Note: we have turned off authentication in [src/main/docker/sonar.yml](src/main/docker/sonar.yml) for out of the box experience while trying out SonarQube, for real use cases turn it back on.

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the gradle plugin.

Then, run a Sonar analysis:

```
./gradlew -Pprod clean check jacocoTestReport sonarqube
```


## Using Docker to simplify development (optional)

You can use Docker to improve your Qualize development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a mysql database in a docker container, run:

```
docker-compose -f src/main/docker/mysql.yml up -d
```

To stop it and remove the container, run:

```
docker-compose -f src/main/docker/mysql.yml down
```

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

```
./gradlew bootJar -Pprod jibDockerBuild
```

Then run:

```
docker-compose -f src/main/docker/app.yml up -d
```
