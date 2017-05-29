# TestUnicredit

# Oprea George

Am implementat toate cele 4 functionalitati si am facut conexiunea dintre
doua baze de date si aplicatia web. Din pacate nu am avut timp sa pun
comentarii in cod pentru ca am avut examen pe data de 28 dar am avut grija sa
numesc sugestiv fiecare functie sau variabila.

#Deschiderea serverului.
La deschidere serverul citeste din doua baze de date informatii despre tickete
si informatii despre evenimente. La fiecare update al informatiilor de pe
server se updateaza si bazele de date (ex: cand adaugam un ticket nou).

#Clasele Ticket si Event.
	Ambele clase implementeaza interfata Stocable pentru a arata ca cele
doua clase vor fi stocate intr-o baza de date si pentru a implementa mai elegant
functia updateDataBase care primteste un ArrayList "stocabil" si il rescrie in
baza de date pentru a o updata.

#Buy_ticket.
	Am presupus ca orice ticket este gratis. Fiecare ticket are asociat un id unic.
Acest lucru l-am implementat folosind o variabila statica in clasa Ticket, astfel
de fiecare data cand adaugam un bilet nou acea variabila se incrementa. Biletul se poate
cumparata numai daca exista in baza de date de evenimente evenimentul corespunzator
biletului. Ex: http//localhost/but_ticket/George/Unicredit .

#List_events.
	Acest serviciu web doar itereaza prin lista de evenimente si o intoarce
(ex:  http//localhost/list_events).

#Ticket_details.
	Aceasta functionalitate am realizat-o iterand prin lista de tichete si afisand
biletul corespunzator userului. (ex http//localhost/ticket_details/George).

#Cancel_ticket
	Pentru a realiza aceasta functionalitate a trebuit sa iterez prin lista de
bilete si sa sterg biletul corespunzator dupa id (ex: http//localhost/cancel_ticket/0).



  
