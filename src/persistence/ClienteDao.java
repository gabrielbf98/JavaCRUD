package persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import entity.Cliente;

public class ClienteDao {
	 
	static EntityManagerFactory factory = Persistence.createEntityManagerFactory("PUjpa");
	
	public static EntityManager getEntityManager() {
		return factory.createEntityManager();
	}
	
	EntityManager manager;
	TypedQuery<Cliente> query;
	
	public void create(Cliente c) throws Exception {
	manager = getEntityManager();
	manager.getTransaction().begin();
	manager.persist(c);
	manager.getTransaction().commit();
	}
	
	public void update(Cliente c) throws Exception {
		manager = getEntityManager();
		manager.clear();
		manager.getTransaction().begin();
		manager.merge(c);
		manager.getTransaction().commit();
		}
	
	public void delete(Cliente c) throws Exception {
		manager = getEntityManager();
		manager.clear();
		manager.getTransaction().begin();
		Cliente resp = manager.find(Cliente.class, c.getId());
		manager.remove(resp);
		manager.getTransaction().commit();
		}
	
	public List<Cliente> findAll(){
		manager = getEntityManager();
		List<Cliente> lista = manager.createQuery("select obj from Cliente as obj",Cliente.class).getResultList();
		return lista;
	}
	
	public Cliente findByCode(Long cod) throws Exception{
		manager = getEntityManager();
		Cliente cliente= manager.find(Cliente.class, cod);
		return cliente;
	}
	
	public static void main(String[] args) {
		try {
			Cliente c1 = new Cliente(3l,null,null);

			
			ClienteDao dao = new ClienteDao();
			dao.delete(c1);
			
			System.out.println("Dados deletados...");
			dao.findAll().stream().forEach(x->System.out.println(x.getNome()+ "," +x.getEmail()));
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("error" + ex.getMessage());
		}
	}
}
