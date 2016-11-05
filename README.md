# OSmsJava
OSmsJava is a library allowing you to consume easily [API REST SmsApi of orange Ivory Coast (zone AMEA)](https://www.orangepartner.com/SMS-CI-API) .
Do not use this library in production. It is under development. It will be available very soon.

## Usage


### Download 

Download via Maven

    <dependency>
        <groupId>org.Akanza</groupId>
        <artifactId>OSmsJava</artifactId>
        <version>0.1.9</version>
    </dependency>

or Gradle

    compile 'org.Akanza:OSmsJava:0.1.9

#### How generate a token

The Token object it is the representation object of the Json response returned by the orange smsAPI.
For generate a Token object, used one of the static methods of Class abstract **`FactoryToken`**.

* **`FactoryToken.getToken(String id,String secretCode)`** : return a simple Token object
* **`FactoryToken.setToken(String id,String secretCode,Callback callback)`** : Instantiate an Token object
* **`FactoryToken.getFutureToken(String id,String secretCode)`** : return a Token Future.

Example with **`FactoryToken.getToken()`** :

    
    String secretCode = "secretCode";
    String id = "id";
    public static void main(String... args)
    {
        try
        {
            Token token  = FactoryToken.getToken();
        }
        catch (ServiceException e)
        {
            ResponseError responseError = e.getResponseError();
            ...................................................
            ...................................................
            e.printStackTrace();
        }
    }


Example with **`FactoryToken.setToken(String id,String secretCode,Callback callback)`** :

    String secretCode = "secretCode";
    String id = "id";
    Token token = null;
    public static void main(String... args)
    {
        Callback callback = new Callback((baseResponse, responseHeader, statusCode) ->
            {
                token = (Token) baseResponse;
                ............
                ............
            });
        Token token = null;
        FactoryToken.setToken(id,secretCode,callback);
        if(token != null)
        {
            String accessToken = token.getAccessToken();
            String tokenType = token.getTokenType();
            ..........................................
        }
    }


Example with **`FactoryToken.getFutureToken(String id,String secretCode)`** :

    String secretCode = "secretCode";
    String id = "id";
    public static void main(String... args)
    {
        try
        {
            Future<Token>  futureToken =  FactoryToken.getFutureToken(id,secretCode);
            ..........................................................................
            ..........................................................................
            ..........................................................................
            Token token = futureToken.get();
            if(token != null)
            {
                String accessToken = token.getAccessToken();
                String tokenType = token.getTokenType();
                ........................................
            }
            ..........................................................................
            ..........................................................................
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(ServiceException es)
        {
            ResponseError responseError = e.getResponseError();
            ...................................................
            ...................................................
            e.printStackTrace();
        }
    }


#### The Callback



#### How Send a SMS : 

The SMS object it is the representation object of your SMS have send.
It is composed of three fields (`String address`, `String senderAddress`,`String content`).

* the field address it is your number phone.
* the field senderAddress it is the number phone recipient
* the field content it is content of message

The ResponseSMS object it is the representation of the Json response returned by  orange smsAPI after having sent a message.
For send a sms Call method `sendSMS(Token token, SMS sms, Callback callback)` of object ServiceSMS.

* The parameter token it is your Token
* The parameter sms it is your sms
* The parameter callback contains the action in executed after the reception of http response

For example:

    public static void main(String... args)
    {
        // Action which perform if response return successful
        OnSuccess onSuccess = (b,r,i) -> {
            ResponseSMS responseSMS = (ResponseSMS) b;
            
            // get out bound sms message request
            SMSResponse outBoundSMSMessageRequest = responseSMS.getOutBoundSMSMessageRequest();
            
            // get sender address
            String senderAddress = outBoundSMSMessageRequest.getSenderAddress();
            
            // get resource Url
            String senderName = outBoundSMSMessageRequest.senderName();
            
            SMSContent smsContent = outBoundSMSMessageRequest.getOutboundSMSTextMessage();
            
            // get sms content
            String message = smsContent.getMessage();
            
            System.out.println("My out bound SMS message request is : "+message);
            System.out.println('The sender address is : "+senderAddress);
            System.out.println("My url resource is : "+senderName);
            ResponseHeader responseHeader = (ResponseHeader) r;
            ....................................................
            ....................................................
            System.out.println("The status code is : "+i);
        }
        
        // Action which perform if response failed
        OnFailure onFailure = (r,m,i,) -> {
            // get message error
            String message = r.getMessage();
            
            // get description error
            String description = r.getDescription();
            
            // get status code error
            int code = r.getCode();
            System.out.println("Error message is : "+message);
            System.out.println("Error description is : "+description);
            System.out.println("The status code is : "+code):
        }
        
        // Action which perform if execution throw exception
        OnThrowable onThrowable = (t) -> {
            // get message of throwable
            String message = t.getMessage();
            ................................
            ................................
            ................................
        }
        
        String id = "";
        String secretCode = "";
        
        // create a callback
        Callback callback = new Callback(onSuccess,onFailure,onThrowable);
        
        // obtain a token
        Token token = FactoryToken.getToken(id,secretCode);
        
        // create a Service SMS
        ServiceSMS serviceSMS = new Service();
        
        // Create a SMS
        SMS sms = new SMS("Your number phone","Number phone of recipient","Your message");
        
        // Send a SMS
        serviceSMS.sendSMS(token,sms,callback);

    }

##### ResponseSMS

The ResponseSMS object it is the representation of the Json response returned by  orange smsAPI after having sent a message.
It contains the different information.
The ResponseSMS contains two Object :

* SMSResponse smsResponse

###### SMSResponse

SMSResponse contains some information. 
SMSResponse contains the four Objects : 

* String address : End user ID
* String senderAddress : The MSISDN of the sender
* String senderName : URL-escaped name of the sender to appear on the terminal is the address to whom a responding SMS may be sent.
* SMSContent smsContent : SMSContent contains the Object String which of sms content
    * String message : The content message
    

#### How consulted numbers sms remainder :

The ContractsSMS object it is the representation object of JSon response returned by orange smsAPI having sent a request of consultation of remaining SMS.
For obtain remaining sms, call method `obtainsContractsSMS(Token token, Callback callback)` of object ServiceSMS.

* The parameter token it is your Token
* The parameter callback contains the action in executed after the reception of http response

At first you have to be interested in the object PartnerContracts. I invite you to glance on the source code.

For example :

    public static void main(String... args)
    {
        // Action which perform if response return successful
        OnSuccess onSuccess = (b,r,i) -> {
            // get ContractsSMS
            ContractsSMS contractSMS = (ContractsSMS) b;
            // get Partner Contract
            PartnerContracts partnerContracts = contractSMS.getPartnerContracts();
            // get array of Contract
            Contract[] contracts = partnerContracts.getContracts();
            // obtain first element of Contract array
            Contract contract = contracts[0];
            
            // get Service
            String service = contract.getService();
            // get contract description
            String contractDescription = contract.getContractDescription();
            // get array of contract service
            ServiceContracts[] serviceContracts = contract.getServiceContracts();
            // get first element of ServiceContracts array
            ServiceContracts  serviceContract = ServiceContracts[0];
            
            // get country 
            String country = serviceContract.getCountry();
            // get service
            String service = serviceContract.getService();
            ..............................................
            ..............................................
            ..............................................
            
        }
        
        // Action which perform if response failed
        OnFailure onFailure = (r,m,i,) -> {
            // get message error
            String message = r.getMessage();
            
            // get description error
            String description = r.getDescription();
            
            // get status code error
            int code = r.getCode();
            System.out.println("Error message is : "+message);
            System.out.println("Error description is : "+description);
            System.out.println("The status code is : "+code):
        }
        
        // Action which perform if execution throw exception
        OnThrowable onThrowable = (t) -> {
            // get message of throwable
            String message = t.getMessage();
            ................................
            ................................
            ................................
        }
        
        String id = "";
        String secretCode = "";
        
        // create a callback
        Callback callback = new Callback(onSuccess,onFailure,onThrowable);
        
        // obtain a token
        Token token = FactoryToken.getToken(id,secretCode);
        
        // create a Service SMS
        ServiceSMS serviceSMS = new Service();
        serviceSMS.obtainsContractsSMS(token,callback);
    }

##### PartnerContracts

The PartnerContracts Object contains some information. 
The PartnerContracts Object contains two Object : 

* String partnerId : Partner Identifier provided by Orange Partner
* Contracts contracts : List of contracts associated to the partner

###### Contracts 

The Contracts Object contain some information of contract associated to the partner.
The Contracts Object contains three Object : 

* String service : Technical name of the subscribed service 
* String contractDescription : Overall service contract description
* ServiceContracts serviceContracts : List of country-specific contracts

###### ServiceContracts

The ServiceContracts Object contain some information.
The ServiceContracts Object contain several Object : 

* String country : ISO 3166-1 alpha 3 country code
* String service : Technical name of the subscribed service
* String contractId : Contract identifier
* int availableUnits : Available units in the contract
* String expires : Contract expiration date, ISO 8601 format
* String scDescription : Country-specific contract description, contains the number of units to consume and the expiration date



#### How consulted the statistics of use of the application (DEPRECATED! Don't Use) :

The StatisticSMS object it is the representation object of JSon response returned by orange smsAPI having sent a request of consultation of statistics ussage.
At first you have to be interested in the object PartnerStatistics. I invite you to glance on the source code.

For example :

    public static void main(String... args)
    {
        try
        {
            StatisticSMS statistics;
            GenerateService service = new GenerateService("5454656","my secret code");
            Token token = service.generatedToken();
            statistics = service.statisticSMS(token);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ServiceException e)
        {
            e.printStackTrace();
        }
    }

#### How consulted the historic purchase (DEPRECATED! Don't Use) :

The HistoricPurchase object it is the representation object of JSon response returned by orange smsAPI having sent a request of consultation of purchase historic.
At first you have to be interested in the object PurchaseOrders. I invite you to glance on the source code.

For example :

    public static void main(String... args)
    {
        try
        {
            HistoricPurchase historic;
            GenerateService service = new GenerateService("5454656","secret code");
            Token token = service.generatedToken();
            historic = service.historicPurchase(token);
            PurchaseOrders[] purchaseOrders = historic.getPurchaseOrders();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ServiceException e)
        {
            e.printStackTrace();
        }
    }
the method `getPurchaseOrders()` return a table of PurchaseOrders.

## Authors and Contributors
In 2015, Amani Christian Cyrille Alves (@DevAlves1993) founded OSmsJava.

## Contacts

* Gmail : [alvesamani@gmail.com] (mailto:alvesamani@gmail.com)
* Twitter [@cyrilleamani] (https://twitter.com/cyrilleamani)

## License

[MIT License](http://www.opensource.org/licenses/mit-license.php) 
[Introduction to the MIT License](https://opensource.org/osd-annotated)

## Contributing
If you would like to contribute code you can do so through GitHub by forking the repository and sending a pull request.
Please also make sure your code compiles by running `mvn clean verify`

The list of the contributions which would be welcome.

* Documentation : Participated in the writing of the documentation.
* Example : Creating several examples showing the use of the library.
* Bugs : Report bugs with of issues.