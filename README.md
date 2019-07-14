# Gobear Travel Insurance
This repository contains [Selenium](http://seleniumhq.org/) tests for the [Gobear - Travel Insurance](https://www.gobear.com/ph?x_session_type=UAT).

## Pre-requisites

This project requires that you use :
* **JDK 8**.
* **Maven 3.5.0**.
* **Selenium 3.14.x** (or) higher.
* **Chrome - lasted version** (Version 75).

### Clone or Download project

Clone the repository using the following command:

```bash
git clone https://github.com/havuong/gobear-travel-insurance.git
```

## Run test with CLI

Verify At Least 3 Cards Displayed
```markdown
$ mvn test -Dgroups=card_displayed
```
Verify Promotions Filter Function
```markdown
$ mvn test -Dgroups=promotions_filter
```
Verify Insurers Filter Function
```markdown
$ mvn test -Dgroups=insurers_filter
```
Verify Range Filter Function
```markdown
$ mvn test -Dgroups=range_filter
```
Verify Sort Function
```markdown
$ mvn test -Dgroups=sort
```
Verify Policy Type Details Function
```markdown
$ mvn test -Dgroups=policy_type
```
Verify Whos Going Details Function
```markdown
$ mvn test -Dgroups=whos_going
```
Verify Destination Details Function
```markdown
$ mvn test -Dgroups=destination
```
Verify Travel Date Details Function
```markdown
$ mvn test -Dgroups=travel_date
```


