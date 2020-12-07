# Smart_home_simulator

A Smart Home Simulation application for SOEN 343 class. This application is built by using javafx and help of other library

# Quick Start
1. Clone the repo
2. Run maven and maven resource (pom.xml)
3. Add VM in the configuration 
  --module-path "PATH TO JAVAFX SDK" --add-modules javafx.controls,javafx.fxml 
4. Run Main class

# Core features
* Upload House Layout
![1](https://user-images.githubusercontent.com/43865591/98489782-76e39180-21fd-11eb-8508-53a7adb6f420.jpg)
1. Error Handling(Exception class)
![Exception](https://user-images.githubusercontent.com/43865591/101296399-4c332b80-37f1-11eb-9e94-400b8b6a7c7c.jpg)

* Set Context of simulation 
1. Choose Date
2. Choose Time
3. Manage User and permision
4. Save User and permision to file,(resuse next launching app)
5. Choose logged user and location
6. Choose Season
7. Choose the start month of the season
8. Choose End month of season
![contextOfsimulation](https://user-images.githubusercontent.com/43865591/101296421-71279e80-37f1-11eb-9310-fe8c3a0d495d.jpg)

* Dashboard
1. SHS
* Change time / date / location of logged user / outside temperature
* Move other user to different room
* Add object block the window 
* change logged user
* adjust Time slider 
![4](https://user-images.githubusercontent.com/43865591/98490052-5962f780-21fe-11eb-8fb5-c0e648c3866a.jpg)

2. SHC
* close open window door
* close open auto light
![3](https://user-images.githubusercontent.com/43865591/98489999-2f113a00-21fe-11eb-9d35-ad046cd4517d.jpg)

![image](https://user-images.githubusercontent.com/43865591/98490111-89aa9600-21fe-11eb-81da-ead8bdc5cc98.png)
3. SHP
* able set away when no user inside the home
* set delay time to call the police 
* set time for light to open 
![5](https://user-images.githubusercontent.com/43865591/98490035-4cde9f00-21fe-11eb-93b1-73a1ae43f9e2.jpg)

4. SHH
4.1 Set up zone 
* add/delete zone 
* add room to zone 
* save/ cancel
![SHH](https://user-images.githubusercontent.com/43865591/101296500-fe6af300-37f1-11eb-8b20-f69298369dba.jpg)
4.2 set desired temperature
* set desired temperature (day morning night) to zone 
* set desired temperature (day morning night) to room
* show/ hide temperatue of zone and room
![dashboard](https://user-images.githubusercontent.com/43865591/101296600-438f2500-37f2-11eb-85d0-1eda6219fa78.jpg)

5. House view 
* Update the icon of door , window , light, logged user, other user, away mode, block window
![image](https://user-images.githubusercontent.com/43865591/98490394-646a5780-21ff-11eb-838d-b9ea1320a6bd.png)
* update the icon of the AC or heating during temperature monitor
![house view](https://user-images.githubusercontent.com/43865591/101296643-84873980-37f2-11eb-8705-f266c73e623d.jpg)

![burse pipe](https://user-images.githubusercontent.com/43865591/101296653-92d55580-37f2-11eb-9500-252a145ec29b.jpg)


# Technologies used
* JavaFx
* JFoenix
* Gson

Member List:
* Samuel Huang 
* Kimchheng heng 
* Badr Saidi 
* Olivier Bertrand-Vachet
* David Rossi 
