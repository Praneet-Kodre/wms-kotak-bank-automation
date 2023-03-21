rmdir /s /q target
mvn clean test -Durl="nbuat.kotak.com/knb2/" -Dsurefire.suiteXmlFiles="iaop_smoke.xml" -Dbrowser="chrome" -Dgroups="smoke"


