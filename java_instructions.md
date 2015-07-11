Get started with CVPicker
-------------------------------------
This is a starter application for Java with Cloudant NoSQL DB service.

The sample is a Favorites Organizer application, that allows users to organize and manage their favorites and supports different types in each category. This sample demonstrates how to access the Cloudant NoSQL DB service that binds to the application, using Cloudant Java APIs.

1. [Install the cf command-line tool](https://www.ng.bluemix.net/docs/#starters/BuildingWeb.html#install_cf).
2. [Download the starter application package](https://console-classic-20150709-192010.ng.bluemix.net:443/rest/../rest/apps/fb82bb70-fbed-4e02-b585-7f10a148be09/starter-download).
3. Extract the package and 'cd' to it.
4. Connect and log in to Bluemix:

		cf login -a https://api.ng.bluemix.net
		
5. Compile the JAVA code and generate the war package using ant.
6. Deploy your app:

		cf push CVPicker -p JavaCloudantDB.war -m 512M

7. Access your app: [CVPicker.mybluemix.net](http://CVPicker.mybluemix.net)
