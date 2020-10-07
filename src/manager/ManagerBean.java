package manager;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.engine.spi.SessionImplementor;

import entity.Cliente;
import net.sf.jasperreports.engine.JasperRunManager;
import persistence.ClienteDao;
import persistence.HibernateUtil;

@ManagedBean(name = "mb")
@RequestScoped
public class ManagerBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Cliente cliente; // um cliente
	private List<Cliente> clientes; // varios clientes

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	//aqui está a busca do banco de dados(prendeu no get)
	//eu posso colocar no clientes --> vem do getClientes()
	public List<Cliente> getClientes() {
		try {
			this.clientes = new ClienteDao().findAll();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}
	
	//composicao
	public ManagerBean() {
		//amarrou o cliente a ManagerBean
		//se acabar mb nao tem cliente
		this.cliente = new Cliente();
	}
	
	//Acoplamento, agregacao
//	public ManagerBean(Cliente c){
//		this.cliente =c;
//	}

	public void reportCliente() {
		FacesContext fc = FacesContext.getCurrentInstance();
		try {
			//desenho
			InputStream arquivo = fc.getExternalContext().getResourceAsStream("/ireportCliente.jasper");
			//detalhes connection
			//relatorio
			SessionImplementor sim = (SessionImplementor) HibernateUtil.getSessionFactory().openSession();
			//jdbc
			//java.sql.Connection
			//5.8 nao pago IReport
			//Tibico Jasper 6.4 ou 6.5
			Connection con = (Connection) sim.connection();
			byte[] report = JasperRunManager.runReportToPdf(arquivo, null, con);
			//atualizar
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
			response.setHeader("context-Disposition", "inline;filename=relatorio.pdf");
			response.setContentLength(report.length);
			//Gerar Byte(arquivo)
			OutputStream out = response.getOutputStream();
			out.write(report);
			out.close();
			//atualiza a tela
			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	public void cadastrar() {
		
		FacesContext fc = FacesContext.getCurrentInstance();
		try {
			//colocar validacao
			new ClienteDao().create(this.cliente);//grava
			//limpa a tela
			this.cliente = new Cliente();
			fc.addMessage(null, new FacesMessage("Dados Gravados"));
		}catch(Exception ex) {
			ex.printStackTrace();
			fc.addMessage(null, new FacesMessage("Error" + ex.getMessage()));
		}
	}
	
	public void alterar() {
		FacesContext fc = FacesContext.getCurrentInstance();
		try {
			new ClienteDao().update(this.cliente);
			this.cliente = new Cliente();
			fc.addMessage(null, new FacesMessage("Dados Alterados"));
		}catch(Exception ex) {
			ex.printStackTrace();
			fc.addMessage(null, new FacesMessage("Error" + ex.getMessage()));
		}
	}
	
	public void excluir() {
		FacesContext fc = FacesContext.getCurrentInstance();
		try {
			new ClienteDao().delete(this.cliente);
			this.cliente = new Cliente();
			fc.addMessage(null, new FacesMessage("Registro Excluidos"));
		}catch(Exception ex) {
			ex.printStackTrace();
			fc.addMessage(null, new FacesMessage("Error" + ex.getMessage()));
		}
	}

}
