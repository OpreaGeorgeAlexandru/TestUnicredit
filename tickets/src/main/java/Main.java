/**
 * Created by Oprea on 29.05.2017.
 */

import org.eclipse.jetty.util.DateCache;

import java.io.BufferedReader;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

interface Stocable{
    String toDataBase();
}

class DataBase{

    private String content;

    public DataBase(String path){
        content = "";
        getContentFromFile(path);
    }

    public  String getContent(){
        return content;
    }

    private void getContentFromFile(String path){
        BufferedReader reader = null;

        try {
            File file = new File(path);
            reader = new BufferedReader(new FileReader(file));

            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                content += line;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class Ticket implements Stocable{

    private String user;
    private String eventName;
    private int ticketNumber;
    private static int currentTicketNumber;

    public Ticket(){
        user = "Unknow";
        eventName = "Unknow";
        ticketNumber = -1;
    }

    public Ticket(String user, String eventName){
        this.user = user;
        this.eventName = eventName;
        currentTicketNumber ++;
        ticketNumber = currentTicketNumber;
    }

    public Ticket(String user,String eventName, int ticketNumber){
        this.user = user;
        this.eventName = eventName;
        currentTicketNumber = ticketNumber;
        this.ticketNumber = ticketNumber;
    }

    public String getUser() {
        return user;
    }

    public String getEventName() {
        return eventName;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    @Override
    public String toString() {
        return user + " event name = " + eventName + " ticketNumber = " + ticketNumber;
    }

    @Override
    public String toDataBase() {
        return "@"+ user + "@" + eventName + "@" +ticketNumber + "@\n";
    }
}

class Event implements Stocable{

    private String name;
    private String location;
    private String time;

    public Event(){
        name = "Unknow";
        location = "In the midle of nowhere";
        time = "01.01.1001";
    }

    public  Event(String name, String location, String time){
        this.name = name;
        this.location = location;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return name + " in " + location + " on " + time + "\n";
    }

    @Override
    public String toDataBase() {
        return "@" +name + "@" + location + "@" + time + "@\n";
    }
}

public class Main {

    private static void updateDataBase(String path, ArrayList<? extends Stocable> elements){

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {

            String content = "";

            for(Stocable st : elements){
                content += st.toDataBase();
            }

            bw.write(content);

            System.out.println("I updated the database");

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    private static ArrayList<Event> getListOfEventsFromString(String data){
        ArrayList<Event> events = new ArrayList<Event>();
        String strings[] = data.split("@");
        for(int it = 0; it+2 < strings.length; it += 3){
            events.add(new Event(strings[it],strings[it + 1],strings[it + 2]));
        }
        return events;
    }

    private static ArrayList<Ticket> getTickets(String data){

        ArrayList<Ticket> tickets = new ArrayList<Ticket>();

        String strings[] = data.split("@");
        for(int it = 0; it + 2 < strings.length; it += 3){
            tickets.add(new Ticket(strings[it],strings[it + 1],Integer.parseInt(strings[it+2])));
        }

        return tickets;
    }

    private static Ticket getUserTicket(String user, ArrayList<Ticket> tickets){

        for(Ticket tk : tickets){
            if(tk.getUser().equals(user)){
                return tk;
            }
        }
        //error
        return null;
    }

    private static String cancelTicket(String idString, ArrayList<Ticket> tickets){

        int id = Integer.parseInt(idString);

        for(int it = 0; it < tickets.size(); it ++){
            if(tickets.get(it).getTicketNumber() == id){
                tickets.remove(it);
                return "Ticket with id = " + id + " was deleted\n";
            }
        }
        return "No ticket with that user found\n";
    }

    private static boolean isInList(String str,ArrayList<Event> strings){

        for(Event it : strings){
            System.out.println(it);
            if(it.getName().equals(str)){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println("starting server...");
        DataBase db = new DataBase("src/ticketDetails.txt");
        ArrayList<Ticket> tickets = getTickets(db.getContent());

        DataBase dataBase = new DataBase("src/listOfEvents.txt");
        ArrayList<Event> events = getListOfEventsFromString(dataBase.getContent());

        get("/ticket_details/:name", (request, respond) -> {

            String user = request.params(":name");

            return getUserTicket(user,tickets).toString();
        });

        get("/list_events", (request, respond) -> {

            String res = "<html><head></head><body><p>List of events: " + "\n";

            for(Event ev : events){
                res += ev.toString();
                System.out.println(ev);
            }

            return res + "</p></body><html>";
        });

        post("/buy_ticket/:user/:eventName", (request, respond) -> {

            String user = request.params(":user");
            String eventName = request.params(":eventName");

            if(!isInList(eventName, events)){
                return "Event dosen't exist\n";
            }

            tickets.add(new Ticket(user,eventName));
            updateDataBase("src/ticketDetails.txt",tickets);

            return "Ticket added";

        });

        post("/cancel_ticket/:id", (request, respond) -> {

            String id = request.params(":id");

            cancelTicket(id,tickets);
            updateDataBase("src/ticketDetails.txt",tickets);

            return "Ticket canceled";
        });

    }
}