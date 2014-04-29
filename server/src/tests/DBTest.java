package tests;

import java.net.UnknownHostException;
import java.sql.SQLException;

import messages.SharedUser;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.Test;

import server.ServerConfiguration;
import server.Server;

import database.MysqlServer;


public class DBTest {

	@Test
	public void test() throws SQLException, ConfigurationException, UnknownHostException {
		Server.serverConfiguration = new ServerConfiguration("./conf/PecuniamExactamServerConf.xml");
		MysqlServer dbserver = new MysqlServer();
		SharedUser su = new SharedUser(true, "admintest", "admintest", -398.8, 0);
		//List <Object[]> results = dbserver.doQuery("SELECT eventTag FROM users WHERE name=? AND digest=PASSWORD(?)", su.getUsername(), su.getPassword());
		//System.out.println(results.get(0)[0]);
		System.out.println(dbserver.getUsers().authentification(su.getUsername(), su.getPassword()));
		dbserver.getUsers().processCash(su.getUsername(), su.getCash());
		System.out.println(dbserver.getProducts().getProductListVersion());
		System.out.println(dbserver.getUsers().getCash(su.getUsername()));
	}

}
