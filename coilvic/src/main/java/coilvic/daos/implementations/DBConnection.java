/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package coilvic.daos.implementations;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import coilvic.controllers.App;
import coilvic.objects.Status;
/**
 *
 * @author ivanr
 */
public class DBConnection {
    private static Connection connection;
    private static final String URL_PROPERTY_FIELD = "mysql.db.url";
    private static final String USER_PROPERTY_FIELD = "vic";
    private static final String PASSWORD_PROPERTY_FIELD = "vic";
}
