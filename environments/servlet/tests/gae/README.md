To run the tests:

        mvn clean test -Dincontainer -Dappengine.sdk.root=<PATH_TO_SDK>

------------------------------------------
To override the default GAE 1.8.9 version:

        mvn clean test -Dincontainer -Dappengine.sdk.root=<PATH_TO_SDK> -Dgae.version=1.8.3
