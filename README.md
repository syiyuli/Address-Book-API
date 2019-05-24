# Address-Book-API

Basic AddressBook RESTful API

Contact.java defines model data
  Model given 4 parameters: Name, Number, Email, and Company
  Name also doubles as ID for REST URL, and therefore must be unique
  Model has inputCheck() method-rudimentary data screening method
    Checks phone numbers to be length between 7 and 10- sets number to "Invalid Number" otherwise
    Checks email String for @ symbol that is not at the beginning or end of the email String- sets email to "Invalid Email" otherwise
  toString() method that displays Contact object in JSON format

StatusResponse.java is an enum that defines SUCCESS and ERROR messages

JsonResponse.java defines creation of JSON status response messages

SparkAPI.java defines availble paths
  POST /contact
  GET /contact/:name
  PUT /contact/:name
  DELETE /contact/:name
  GET /contact/:pageSize/:page
    returns all contacts in appropriate page
  GET /contact/:pageSize/:page/:query
    returns contacts in appropriate page based on query
    query should be given as String in format "q={field}:{value}" in order to pass to Elasticsearch
  
ContactService.java defines logic for creating and sending requests to Elasticsearch instance
  Creates RestClient and sends Requests through RestClient to specified host, port, and endpoints
  Returns JSON status responses or JSON Contacts as appropriate

UnitTest.java defines unit tests for functionality
  Tests include:
    Adding contacts
    Getting contacts
    Invalid phone number
    Invalid email
    Updating contacts
    Getting all contacts
    Getting correct number of contacts per page
    Getting all contacts on correct page
    Getting all contacts with query
