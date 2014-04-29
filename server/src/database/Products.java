package database;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import messages.SharedProduct;

/**
 * Classe représentant la table products dans la base de donnée.
 * Assume que la BDD est correctement configurée.
 * 
 * @author Mickaël Misbach
 *
 */
public class Products extends MysqlTable {

	/**
	 * Constructeur par défaut.
	 * 
	 * @param dbServer le serveur mysql
	 */
	public Products(MysqlServer dbServer) {
		super(dbServer);
	}
	
	/**
	 * @return la version de la liste des produits
	 */
	public int getProductListVersion() {
		try {
			List<Object[]> results = this.dbServer.doQuery("SELECT price FROM products WHERE tag='product_list_version'");
			return ((BigDecimal) results.get(0)[0]).intValue();
		} catch (SQLException e) {
			this.dbError(e);
			return -1;
		}
		
	}
	
	/**
	 * Retourne la liste des tags des produits associés à un stand.
	 * 
	 * @param standTag le tag du stand
	 * 
	 * @return la liste des tags des produits associés 
	 */
	public List<String> getProductsByStand(String standTag) {
		try {
			List<Object[]> results = this.dbServer.doQuery("SELECT tag FROM products WHERE standTag=?", standTag);
			List<String> returnList = new ArrayList<String>();
			
			for (int i = 0 ; i < results.size() ; i++)
				returnList.add((String) results.get(i)[0]);
				
			return returnList;
		} catch (SQLException e) {
			this.dbError(e);
			return null;
		}
	}
	
	/**
	 * Retourne le nom du produit avec son tag
	 * 
	 * @param productTag le tag du produit
	 * 
	 * @return le nom du produit
	 */
	public String getProductNameByTag(String productTag) {
		try {
			List<Object[]> results = this.dbServer.doQuery("SELECT name FROM products WHERE tag=?", productTag);
			return (String) results.get(0)[0];
		} catch (SQLException e) {
			this.dbError(e);
			return "";
		}
	}
	
	/**
	 * Retourne l'unité du produit avec son tag
	 * 
	 * @param productTag le tag du produit
	 * 
	 * @return l'unité du produit
	 */
	public String getProductUnitByTag(String productTag) {
		try {
			List<Object[]> results = this.dbServer.doQuery("SELECT unit FROM products WHERE tag=?", productTag);
			return (String) results.get(0)[0];
		} catch (SQLException e) {
			this.dbError(e);
			return "";
		}
	}

	/**
	 * Retourne la description du produit avec son tag
	 * 
	 * @param productTag le tag du produit
	 * 
	 * @return la description du produit
	 */
	public String getProductDescriptionByTag(String productTag) {
		try {
			List<Object[]> results = this.dbServer.doQuery("SELECT description FROM products WHERE tag=?", productTag);
			return (String) results.get(0)[0];
		} catch (SQLException e) {
			this.dbError(e);
			return "";
		}
	}
	
	/**
	 * Retourne le prix du produit avec son tag
	 * 
	 * @param productTag le tag du produit
	 * 
	 * @return le prix du produit
	 */
	public double getProductPriceByTag(String productTag) {
		try {
			List<Object[]> results = this.dbServer.doQuery("SELECT price FROM products WHERE tag=?", productTag);
			return ((BigDecimal) results.get(0)[0]).doubleValue();
		} catch (SQLException e) {
			this.dbError(e);
			return -1;
		}
	}
	
	/**
	 * Retourne le produit avec son tag
	 * 
	 * @param productTag le tag du produit
	 * 
	 * @return le produit
	 */
	public SharedProduct getProductByTag(String productTag) {
		return new SharedProduct(false, this.getProductNameByTag(productTag), productTag, 
				this.getProductUnitByTag(productTag), this.getProductDescriptionByTag(productTag), this.getProductPriceByTag(productTag));
	}
}
