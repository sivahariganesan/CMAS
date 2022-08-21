/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crisisProcessor.processAlert;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author HP
 */
public class ProcessLocation 
{        
    private HashSet<String> userMobile=new HashSet<>();
    private FileHandler fileHandler;
    Logger logger=Logger.getLogger(ProcessLocation.class.getName());
    public void getUsersInCrisis(String pincode)
    {                
        try 
        {
            fileHandler=new FileHandler("C:\\xampp\\tomcat\\logs\\MyLogFile.log"); 
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();  
            fileHandler.setFormatter(formatter);  

            Double centerLatitude=0.0;
            Double centerLongitude=0.0;
            String url="jdbc:mysql://localhost:3306/";
            String user="root";
            String password="";
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            statement.execute("USE CMAS");
            ResultSet result=statement.executeQuery("SELECT * FROM pincode where pincode="+pincode);
            while(result.next())
            {
                centerLatitude=result.getDouble("latitude");
                centerLongitude=result.getDouble("longitude");
            }
            Double radius=0.0005;
            String userInCrisisQuerry="SELECT mobilenumber FROM gpslocation where (power((latitude-"+centerLatitude+"),2)+power((longitude-"+centerLongitude+"),2)-"+radius+")<=0";
            ResultSet userInCrisis=statement.executeQuery(userInCrisisQuerry);
            System.out.println("Result Set:"+userInCrisis.toString());
            logger.info("Result Set:"+userInCrisis.toString());
            while(userInCrisis.next())
            {
                userMobile.add(userInCrisis.getString("MobileNumber"));
                logger.info("Mobile Number:"+userInCrisis.getString("MobileNumber"));
            }
            
        }
        catch (SQLException ex) 
        {
            logger.severe(ex.getLocalizedMessage());
        }
        catch (ClassNotFoundException ex) 
        {
            logger.severe(ex.getLocalizedMessage());
        } catch (InstantiationException ex) {
            logger.severe(ex.getLocalizedMessage());
        }
        catch (IllegalAccessException ex) 
        {
            logger.severe(ex.getLocalizedMessage());
        } catch (IOException ex) {
            logger.severe(ex.getLocalizedMessage());
        } catch (SecurityException ex) {
            logger.severe(ex.getLocalizedMessage());
        }
    }
    public void alarmCrisis(String pincode,String crisis)
    {
        getUsersInCrisis(pincode);
        Iterator mobileNumber=userMobile.iterator();
        SendSms smsSender=new SendSms();
        smsSender.sendSms("9600488613", crisis);
        while(mobileNumber.hasNext())
        {
            String mobile=(String) mobileNumber.next();
            logger.info("Sending SMS to mobile");
            smsSender.sendSms(mobile, crisis);            
        }
        fileHandler.close();
    }
}
