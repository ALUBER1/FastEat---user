# FastEat---user
Questo e' il software utente dell'app fasteat<br/>
le seguenti sono le classi usate e i loro attributi e metodi:<br/>
![user class uml diagram](src/main/resources/user%20class%20uml.png)
queste classi sono usate per instaurare una connessione con il server di cui va scritto l'url (SERVER_URL) e la porta (PORT) in cui e' in ascolto per connessioni tcp nel file .env che si trova in [questo percorso](src/main/resources/.env).<br/>
Le classi interagiscono in questo modo per la registrazione di un utente:<br/>
![diagramma di flusso registrazione](src/main/resources/initial%20setup%20uml.png)<br/>
Mentre invece per l'invio di ordine il flusso e' il seguente:<br/>
![diagramma di flusso invio ordine](src/main/resources/ordine%20uml.png)<br/>
<br/>
## ESEMPI DI JSON
L'app per comunicare usa messaggi con un body json che dal server contengono anche un codice che puo' specificare informazioni in piu' come errori del server.<br/>
Un esempio di un json del [login](src/main/java/models/dto/LoginDto.java) potrebbe essere:
```json
{
    "username": "test",
    "type": "user"
}
```
dove lo username specifica il nome utente inserito dall'utente e il type specifica se e' uno user o un rider.<br/>
Un altro tipo di messaggio inviato dall'utente e' [OrdineRequestDto](src/main/java/models/dto/OrdineRequestDto.java), ad esempio:<br/>
```json
{
    "descrizione": "2 x pasta,\n3 x pizza"
}
```
la descrizione contiene informazioni serializzate su tutti i prodotti presenti nell'ordine<br/><br/>
## AVVIARE IL PROGETTO
Per avviare il progetto serve java 22 installato, dopodiche' basta eseguire il file [Main.java](src/main/java/Main.java), naturalmente solo dopo aver inserito le variabili nel file .env come descritto precedentemente.
