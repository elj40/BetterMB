# BetterMB
Project that aims to improve on Stellenbosch University's current meal booking
system.  
Comes with a client that is seperated from frontends so that custom
frontends can be implemented easily.

## Dependencies
To run BetterMB, you need the following installed on your computer:
* Java 21 JDK: [Download here](https://www.oracle.com/java/technologies/downloads/#java21)
(Make sure it's added to your system's PATH)
* Google Chrome: [Download here](https://www.google.com/chrome/)

## Installation and Setup

### Running Precompiled Releases

BetterMB releases may include **fat-jars** or **JRE images**.

#### For Fat-Jars
Use the fat-jar if you already have java installed
* save the `bettermb-<version_info>.jar` file wherever you like
* run `java --version` (ensure you are using Java 21 or higher)
* run `java -jar bettermb-<version_info>.jar`

#### For JRE Images
Use the JRE image if you do not have Java (cannot guarantee it'll work)
* extract the archive
* navigate to the `bin` directory inside the extracted directory
* run the `bettermb` script:
    * Unix: `./bettermb`
    * Windows: `./bettermb.bat` (double-click or terminal)

### Build from source
1. Download the source code (either with `git clone` or download the zip)
2. Run `./gradlew build` or `./gradlew.bat build`
3. Run `./gradlew :cli:run` for the cli
3. Run `./gradlew :gui:run` for the gui

## Contributing to BetterMB

### Contribution Process
1. Fork the repository.
2. Create a feature branch.
3. Submit a pull request with a clear description.

### Code Standards
- The code quality as of right now is not very high, but the bare minimum is
  still to have readable code with consistent naming conventions.

## Issue Tracking
- Use the issue tracker for reporting bugs or suggesting features.
- Label issues appropriately.

## Documentation
- You don't have to comment everything, but please make sure its readable

## Licensing
- All contributions must comply with the GNU GPLv3 (software that uses this code must be open source on request).

## Review Process
- Expect a code review before merging.
- Be open to feedback and discussions.

## Communication
- Message me on Whatsapp
- Send me an email at elaijoubert@gmail.com

## Acknowledgment
- Contributors will be acknowledged in the CONTRIBUTORS.md file.

Thank you for your contributions!
