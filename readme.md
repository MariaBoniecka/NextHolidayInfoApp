1. To build project, run following command: mvn clean package
2. To build docker image, run command: docker build -t nextholidayapp .
3. Start container with command: docker run -p 8080:8080 nextholidayapp --name nextholidayapp
4. Import Postman collection from file NextHolidaysApp.postman_collection.json


API documentation: https://holidayapi.com/docs


Assumptions:
1. Due to restrictions from external API provider, this API shows only holiday from previous year.
2. As the API provider gives only 10,000 free requests, I'm reducing the amount of requests as much as possible (for example I'm checking the list of available countries only once with the first request, or I'm always getting holiday list for the whole selected year).
