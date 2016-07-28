/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.leilao.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.gov.to.detran.controller.BaseController;
import br.gov.to.detran.leilao.domain.Categoria;
import br.gov.to.detran.leilao.domain.ItemLeilao;
import br.gov.to.detran.leilao.domain.QItemLeilao;
import br.gov.to.detran.leilao.domain.Situacao;
import br.gov.to.detran.leilao.repository.CategoriaRepository;
import br.gov.to.detran.leilao.repository.ItemLeilaoRepository;
import br.gov.to.detran.repository.DetranNETRepository;
import br.gov.to.detran.repository.Repository;
import br.gov.to.detran.util.FacesUtil;

/**
 *
 * @author maycon
 */
@Named
@ViewScoped
public class ItemLeilaoController extends BaseController<ItemLeilao> implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    
	private @Inject ItemLeilaoRepository repository;
	private @Inject CategoriaRepository categoriaRepository;
    private @Inject DetranNETRepository detranRepository;
    private Categoria categoria;
    private String placa; 
    private List<Object[]> veiculos;

    @PostConstruct
    public void postConstruct() {
        super.postContruct();
        
        ItemLeilao itemLeilao = this.getFlash("itemLeilaoInstance");
        if (itemLeilao != null) {
            this.instance = itemLeilao;            
        }           
    }
    
    @SuppressWarnings("unchecked")
	public void checkCategoria() throws IOException {
        try {
            if (this.categoria == null || this.categoria.getId() == 0) {
                this.categoria = this.categoriaRepository.getCategoria(Long.parseLong(id));
                if (this.categoria == null) {
                    redirectWithMensagem("Categoria não foi encontrado!", FacesUtil.ERROR);
                }
                list.getPredicate().add(QItemLeilao.itemLeilao.categoria.id.eq(this.categoria.getId()));
            }
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
            this.redirectWithMensagem("Categoria não foi encontrado!", FacesUtil.ERROR);        
        } catch (Exception ex) {
            ex.printStackTrace();
            redirectWithMensagem("Categoria não foi encontrado!", FacesUtil.ERROR);
        }
    }
    
    public void redirectWithMensagem(String title, FacesMessage.Severity s) throws IOException {
        addMenssage(s, "Chamado", title);
        redirect("/leilao/edit.jsf");
    }
    
    public void voltarLista(){        
        try {
        	if(categoria != null){
        		redirect("/leilao/veiculo/list.jsf?id=" + String.valueOf(categoria.getId()));
        	}else{
        		redirect("/leilao/categoria/list.jsf");
        	}        	
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void insert() {
        try {        	
        	this.instance.setCategoria(categoria);
            this.instance.setCreated(new Date());
            this.instance.setUpdated(new Date());            
            this.repository.insert(this.instance);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Registro Adicionado", "Cadastro");
            this.instance = new ItemLeilao();
            this.placa = "";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void update() {
        try {        	
            this.instance.setUpdated(new Date());            
            this.repository.update(this.instance);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Registro Atualizado", "Atualização");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void removeAndRedirect(ItemLeilao instance, String link){        
        try {
            this.remove(instance);
            this.redirect(link);
        } catch (IOException ex) {
            Logger.getLogger(ItemLeilaoController.class.getName()).log(Level.SEVERE, null, ex);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Não foi possivel redirecionar para a página", "Redirecionar");
        }
    }

    public void remove(ItemLeilao instance) {
        try {
            this.repository.remove(instance, true);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Registro Excluído", "Exclusão");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void viewVeiculoEdit(ItemLeilao itemLeilao, String url) {
        try {
        	if(itemLeilao != null){
        		setFlash("itemLeilaoInstance", itemLeilao);
        	}	
        	if(categoria != null){
        		this.redirect(url + "?id=" + String.valueOf(categoria.getId()));
        	}else{
        		this.redirect(url);
        	}            
        } catch (IOException ex) {
            addMenssage(FacesMessage.SEVERITY_ERROR, "Redirecionar", "Não foi possivel redirecionar para o chamado");
            Logger.getLogger(BaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void pesquisarPlaca(){    	
    	this.pesquisarVeiculo(placa, null);
    }
    
    public void pesquisarChassi(){
    	this.pesquisarVeiculo(null, placa);    	
    }
    
    public void pesquisarVeiculo(String placa, String chassi){
    	try{
    		if(placa != null  && !placa.isEmpty() && repository.existePlacaCadastrada(placa)){
    			throw new Exception("Placa já cadastrada!");
    		}
    		if(placa != null && !chassi.isEmpty() && chassi != null && repository.existeChassiCadastrado(chassi)){
    			throw new Exception("Chassi já cadastrado!");
    		}
    		
    		Object[] veiculo = null;
    		if(placa != null && !placa.isEmpty()){
    			veiculo = detranRepository.pesquisarVeiculoPlaca(placa);
    		}    		
    		if(chassi != null && !chassi.isEmpty()){
    			veiculo = detranRepository.pesquisarVeiculoChassi(chassi);
    		}
        	ItemLeilao item = new ItemLeilao();
        	item.setPlaca(String.valueOf(veiculo[3]));    	
        	item.setChassi(String.valueOf(veiculo[2]));
        	item.setRenavam(String.valueOf(veiculo[1]));
        	item.setProprietario(String.valueOf(veiculo[13]));
        	item.setMarcaModelo(String.valueOf(veiculo[4]));
        	item.setCor(String.valueOf(veiculo[5]));
        	item.setCnpj(String.valueOf(veiculo[14]));
        	item.setUf(String.valueOf(veiculo[11]));
        	item.setUf2(String.valueOf(veiculo[18]));
        	item.setMotor(String.valueOf(veiculo[15]));
        	item.setAnoModelo(String.valueOf(veiculo[9])+"/"+String.valueOf(veiculo[10]));    	
        	String tipoLog = String.valueOf(veiculo[19]);
        	String log = String.valueOf(veiculo[16]);
        	String numero = String.valueOf(veiculo[21]);
        	String bairro = String.valueOf(veiculo[22]);
        	
        	item.setEndereco(tipoLog + " " + log + ", n° " + numero + " - " + bairro);
        	item.setCep(String.valueOf(veiculo[17]));
        	item.setCidade((String) veiculo[12]);
        	String restricao1 = (String) veiculo[25];
        	String restricao2 = (String) veiculo[26];
        	String restricao3 = (String) veiculo[27];
        	String restricao4 = (String) veiculo[28];
        	item.setRestricao(restricao1 + ";" + restricao2 + ";" +restricao3 + ";" +restricao4);
        	item.setMulta(((BigDecimal) veiculo[32]).doubleValue());
        	item.setIpva(((BigDecimal) veiculo[30]).doubleValue());
        	item.setDpvat(((BigDecimal) veiculo[31]).doubleValue());
        	item.setTotalDebitosDetran(((BigDecimal) veiculo[29]).doubleValue());
        	item.setTiporestricao(String.valueOf(veiculo[23]));
        	item.setAlienacao(String.valueOf(veiculo[24]));
        	item.setDigitador(FacesUtil.loggedUser().getName());
        	Integer tipoPessoa = 2;
        	if(veiculo[36] instanceof String){
        		if(((String) veiculo[36]).equalsIgnoreCase("Pessoa Física (CPF)")){
        			tipoPessoa = 1;
        		}
        	}else if(veiculo[36] instanceof Integer){
        		if(((Integer) veiculo[36]) == 1){
        			tipoPessoa = 1;
        		}
        	}else if(veiculo[36] instanceof BigDecimal){
        		if(((BigDecimal) veiculo[36]).intValue() == 1){
        			tipoPessoa = 1;
        		}
        	}
        	if(tipoPessoa == 1){
        		String cnpj = item.getCnpj();        		        		    
        		if(cnpj.length() > 11){
        			String cpf= cnpj.substring(Math.max(0, cnpj.length() - 11));
        			item.setCnpj(cpf);
        		}        		
        	}        	
        	this.instance = item;
    	}catch(Exception e){
    		e.printStackTrace();
    		FacesContext.getCurrentInstance().validationFailed();
    		addMenssage(FacesMessage.SEVERITY_ERROR, "Validação", e.getMessage());
    	}
    	
    }
    
    public StreamedContent exportar(){
    	String[] placas = {"ADA8369",
    			"AKP6410",
    			"ALG8129",
    			"ALN0625",
    			"BIA3339",
    			"BPY8432",
    			"BXK6440",
    			"CGF6905",
    			"CGZ4179",
    			"CHF1134",
    			"CHS1748",
    			"CHY7318",
    			"CML2944",
    			"CPC2735",
    			"CQP7784",
    			"CSP3171",
    			"CTL4027",
    			"CVM3872",
    			"CWY9931",
    			"CYI2996",
    			"CZO1728",
    			"DDA8536",
    			"DDZ2602",
    			"DIH2976",
    			"DPI1256",
    			"ECN5142",
    			"EHZ0803",
    			"EKT3048",
    			"EOX0845",
    			"GOR9736",
    			"GUQ3350",
    			"GUX7764",
    			"GYE7051",
    			"GZB6229",
    			"GZR3903",
    			"HBE2345",
    			"HEI9947",
    			"HGD9796",
    			"HGI3724",
    			"HOZ0717",
    			"HPB2906",
    			"HPE4944",
    			"HPG5466",
    			"HPI8029",
    			"HPL9080",
    			"HPM5341",
    			"HPP0347",
    			"HPW7643",
    			"HPX4042",
    			"HPZ0460",
    			"HPZ3692",
    			"HPZ8124",
    			"HRN4216",
    			"HWL6466",
    			"HXT8502",
    			"HZB0098",
    			"IMI4801",
    			"ITB1428",
    			"JEK6550",
    			"JFL4995",
    			"JFQ9063",
    			"JFT5932",
    			"JFW3558",
    			"JGH2658",
    			"JGI3927",
    			"jgo3116",
    			"JHC7214",
    			"JHG1724",
    			"jhx3424",
    			"JJD5659",
    			"JJI1343",
    			"JLI1849",
    			"JLX9892",
    			"JMG9698",
    			"JOD6477",
    			"JTA2388",
    			"JTC9169",
    			"JTM3865",
    			"JTO3577",
    			"JTY9154",
    			"JUB2361",
    			"JUB7528",
    			"JUC4761",
    			"JUK9996",
    			"JUQ1329",
    			"JUW4079",
    			"JVE6590",
    			"JVH1551",
    			"JVI9273",
    			"JVO9084",
    			"JVR7325",
    			"JVT1743",
    			"JYQ9902",
    			"KAD5500",
    			"kag7256",
    			"KAK0987",
    			"KAO1981",
    			"KAW1446",
    			"KBI1471",
    			"KBM6498",
    			"KBM7655",
    			"kbs2889",
    			"KBY5937",
    			"KBY7266",
    			"KCJ0212",
    			"KCJ3795",
    			"KCQ2107",
    			"KCV6991",
    			"KDE0846",
    			"KDE2458",
    			"KDI6325",
    			"KDO7499",
    			"KDT7147",
    			"KDW3376",
    			"KDX3949",
    			"KDX9226",
    			"KDY1391",
    			"KDZ1975",
    			"KEB0967",
    			"KEB1120",
    			"KED3554",
    			"KEE1844",
    			"KEG5705",
    			"KEK9515",
    			"KEL4186",
    			"KEN5400",
    			"KEP6824",
    			"KET2309",
    			"KEX7578",
    			"KEY8346",
    			"KFO5144",
    			"KLE5997",
    			"KLM8274",
    			"KPW0315",
    			"KRT0698",
    			"LCZ8070",
    			"LKM1535",
    			"LPA1389",
    			"LPL1314",
    			"LVF9891",
    			"LVH9689",
    			"LVQ3985",
    			"MBB3798",
    			"MNS7488",
    			"MVL4812",
    			"MVL7377",
    			"MVL7944",
    			"MVL8278",
    			"MVL9176",
    			"MVL9989",
    			"MVM3884",
    			"MVM6636",
    			"MVM8713",
    			"MVN2120",
    			"MVN8154",
    			"MVN9443",
    			"MVO0134",
    			"MVO1386",
    			"MVO2902",
    			"MVO3749",
    			"MVO5379",
    			"MVO6281",
    			"MVO8173",
    			"MVO8253",
    			"MVO8854",
    			"MVO8893",
    			"MVO9516",
    			"MVP0645",
    			"MVP2581",
    			"MVP4737",
    			"MVP6511",
    			"MVP6563",
    			"MVP7702",
    			"MVP8539",
    			"MVP8646",
    			"MVP8802",
    			"MVQ2076",
    			"MVQ3607",
    			"MVQ4135",
    			"MVQ4793",
    			"MVQ7993",
    			"MVR0132",
    			"MVR0651",
    			"MVR0762",
    			"MVR1203",
    			"MVR1478",
    			"MVR1782",
    			"MVR5175",
    			"MVR6899",
    			"MVR6927",
    			"MVR8843",
    			"MVR9469",
    			"MVS1279",
    			"MVS1547",
    			"MVS4823",
    			"MVS5118",
    			"MVS5752",
    			"MVS5911",
    			"MVS8123",
    			"MVS8811",
    			"MVS9007",
    			"MVT0002",
    			"mvt0533",
    			"MVT1134",
    			"MVT2733",
    			"MVT3956",
    			"MVT6761",
    			"MVT7287",
    			"MVT8363",
    			"MVU0478",
    			"MVU0847",
    			"MVU4973",
    			"MVU5025",
    			"MVU6356",
    			"MVU6639",
    			"MVU7710",
    			"MVU8341",
    			"MVV0148",
    			"MVV0858",
    			"MVV0878",
    			"MVV2020",
    			"MVV2040",
    			"MVV2678",
    			"MVV6623",
    			"MVV7946",
    			"MVW0110",
    			"MVW0520",
    			"mvw1608",
    			"MVW1969",
    			"MVW2455",
    			"MVW3310",
    			"MVW4368",
    			"MVW8148",
    			"MVW8338",
    			"MVW8690",
    			"MVW9690",
    			"MVX2573",
    			"MVX3771",
    			"MVX5006",
    			"MVX5037",
    			"MVX5226",
    			"MVX7773",
    			"MVY1286",
    			"MVY2287",
    			"MVY2317",
    			"MVY3968",
    			"MVY4613",
    			"MVY4876",
    			"MVY4909",
    			"MVY5623",
    			"MVY6009",
    			"MVY6396",
    			"MVY7262",
    			"MVY8603",
    			"MVY8948",
    			"MVZ0494",
    			"MVZ0748",
    			"MVZ1924",
    			"MVZ3888",
    			"MVZ4511",
    			"MVZ5001",
    			"MVZ5640",
    			"MVZ7173",
    			"MVZ7740",
    			"MVZ8247",
    			"MVZ8325",
    			"MVZ8425",
    			"MVZ8830",
    			"MWA1280",
    			"MWA2990",
    			"MWA3593",
    			"MWA5400",
    			"MWA6407",
    			"MWA6867",
    			"MWA6968",
    			"MWA7634",
    			"MWA7928",
    			"MWA9376",
    			"MWB0991",
    			"MWB1862",
    			"MWB2189",
    			"MWB2358",
    			"MWB5045",
    			"MWB5756",
    			"MWB6023",
    			"MWB6096",
    			"MWB8318",
    			"MWB8520",
    			"MWB8675",
    			"MWC2230",
    			"MWC2341",
    			"MWC3349",
    			"MWC4158",
    			"MWC4936",
    			"MWC5535",
    			"MWC5938",
    			"MWC6132",
    			"MWC6411",
    			"MWC6780",
    			"MWC7670",
    			"MWC8180",
    			"MWC9101",
    			"MWC9659",
    			"MWC9675",
    			"MWD1450",
    			"MWD1916",
    			"MWD2280",
    			"MWD3957",
    			"MWD9058",
    			"MWD9588",
    			"MWE1990",
    			"MWE2100",
    			"MWE3501",
    			"MWE4543",
    			"MWE6846",
    			"MWE7720",
    			"MWE8227",
    			"MWE9090",
    			"MWE9546",
    			"MWF0015",
    			"MWF1879",
    			"MWF2239",
    			"MWF2950",
    			"MWF3277",
    			"MWF4342",
    			"MWF7877",
    			"MWF7929",
    			"MWG0325",
    			"MWG0408",
    			"MWG1055",
    			"MWG1730",
    			"MWG2466",
    			"MWG2650",
    			"MWG3429",
    			"MWG4277",
    			"MWG4843",
    			"MWG5517",
    			"MWG5773",
    			"MWG6261",
    			"MWG6711",
    			"MWG8964",
    			"MWH0006",
    			"MWH0229",
    			"MWH0856",
    			"MWH1814",
    			"MWH2223",
    			"MWH2298",
    			"MWH3001",
    			"MWH3184",
    			"MWH3281",
    			"MWH3316",
    			"MWH3360",
    			"MWH3879",
    			"MWH3965",
    			"MWH4021",
    			"MWH5105",
    			"MWH5342",
    			"MWH5658",
    			"MWH6081",
    			"MWH6704",
    			"MWH8440",
    			"MWH9325",
    			"MWH9486",
    			"MWI1861",
    			"MWI2531",
    			"MWI2987",
    			"MWI3307",
    			"MWI5917",
    			"MWI7370",
    			"MWI7711",
    			"MWI9691",
    			"MWI9820",
    			"MWJ0500",
    			"MWJ1201",
    			"MWJ2863",
    			"MWJ3057",
    			"MWJ3276",
    			"MWJ3330",
    			"MWJ3591",
    			"MWJ3653",
    			"MWJ4479",
    			"MWJ4573",
    			"MWJ5257",
    			"MWJ9405",
    			"MWK3626",
    			"MWK3714",
    			"MWK4584",
    			"MWK5679",
    			"MWK8063",
    			"MWK8267",
    			"MWK8997",
    			"MWK9010",
    			"MWK9197",
    			"MWL0198",
    			"MWL3018",
    			"MWL4247",
    			"MWL5622",
    			"MWL7140",
    			"MWM0224",
    			"MWM0313",
    			"MWM2228",
    			"MWM2937",
    			"MWM4067",
    			"MWM4862",
    			"MWM5283",
    			"MWM5407",
    			"MWM6022",
    			"MWM6134",
    			"MWM8737",
    			"MWM8856",
    			"MWN0845",
    			"MWN2217",
    			"MWN2221",
    			"MWN2303",
    			"MWN3834",
    			"MWN4819",
    			"MWN8743",
    			"MWN9344",
    			"MWN9523",
    			"MWN9763",
    			"MWN9863",
    			"MWO0625",
    			"MWO1483",
    			"MWO1985",
    			"MWO3633",
    			"MWO3788",
    			"MWO6473",
    			"mwo7430",
    			"MWO8174",
    			"MWO8303",
    			"MWO9526",
    			"MWP1353",
    			"MWP2249",
    			"MWP3542",
    			"MWP3591",
    			"MWP5050",
    			"MWP5377",
    			"MWP5565",
    			"MWP5784",
    			"MWP6919",
    			"MWP7706",
    			"MWP7782",
    			"MWP7998",
    			"MWP8681",
    			"MWQ0748",
    			"MWQ2444",
    			"MWQ3483",
    			"MWR0650",
    			"MWR1142",
    			"MWR1461",
    			"MWR1788",
    			"MWR5164",
    			"MWR9334",
    			"MWS2101",
    			"MWS3640",
    			"MWS3722",
    			"MWS4488",
    			"MWS4816",
    			"MWS8173",
    			"MWT0026",
    			"MWT3697",
    			"MWT3825",
    			"MWT4279",
    			"MWT6328",
    			"MWU4601",
    			"MWU5745",
    			"MWU6958",
    			"MWU8488",
    			"MWU9899",
    			"MWV2483",
    			"MWV5776",
    			"MWV6010",
    			"MWV6199",
    			"MWV7135",
    			"MWW1456",
    			"MWW1904",
    			"MWW2248",
    			"MWW4464",
    			"MWW5814",
    			"MWW8271",
    			"MWX0638",
    			"MWX3170",
    			"MWX3807",
    			"MWX4750",
    			"MWX7230",
    			"MWX8658",
    			"MWY1321",
    			"MWY1709",
    			"MWY2003",
    			"MWY2568",
    			"MWY6052",
    			"MWZ0314",
    			"MWZ0987",
    			"MWZ3540",
    			"MWZ4373",
    			"MWZ6166",
    			"MWZ6534",
    			"MWZ6865",
    			"MWZ6882",
    			"MWZ7422",
    			"MXA0325",
    			"MXA0638",
    			"MXA9483",
    			"MXA9690",
    			"MXB4184",
    			"MXB5359",
    			"MXB5815",
    			"MXC0167",
    			"MXC0487",
    			"MXC1991",
    			"MXC2320",
    			"MXC4681",
    			"MXC5522",
    			"MXC6333",
    			"MXC7117",
    			"MXC8881",
    			"MXC9433",
    			"MXD0990",
    			"MXD1493",
    			"MXD8022",
    			"MXD8804",
    			"MXD8863",
    			"MXD9764",
    			"MXE0186",
    			"MXE1000",
    			"MXE1424",
    			"MXE2833",
    			"MXE3807",
    			"MXE4450",
    			"MXE5420",
    			"MXE6425",
    			"MXF1961",
    			"MXF4696",
    			"MXF6086",
    			"mxf7178",
    			"MXF8291",
    			"MXF9056",
    			"MXF9332",
    			"NFC3909",
    			"NFD9410",
    			"NFG5852",
    			"NFM5940",
    			"NFM7685",
    			"NFO6128",
    			"NFT2003",
    			"NGG9325",
    			"NGM1978",
    			"NGP7217",
    			"NGR4074",
    			"NGU0238",
    			"NGX2863",
    			"NGX5804",
    			"NHA3539",
    			"NIB5746",
    			"NID9495",
    			"NJG5898",
    			"NJX8395",
    			"NKE4541",
    			"nke7599",
    			"NKQ9655",
    			"NLC0376",
    			"NLC1952",
    			"NLF1902",
    			"NLM2075",
    			"NLM5988",
    			"NMV6052",
    			"NSK2643",
    			"NSN0320",
    			"NSQ7240",
    			"NSU2526",
    			"NTC9470",
    			"NVW2528",
    			"NWG7714",
    			"NWG9789",
    			"NWK3830",
    			"NWN6952",
    			"NWQ8345",
    			"OBU4102",
    			"OBX0848",
    			"OED2673",
    			"ofl4865",
    			"OLH1341",
    			"OLH3827",
    			"OLI1659",
    			"OLI2816",
    			"OLJ5437",
    			"OLK5062",
    			"OLK7248",
    			"OLK7787",
    			"OLK7927",
    			"OLL8045",
    			"OLN5076",
    			"OLN8232",
    			"OLN8519",
    			"OLN8798",
    			"OYA5168",
    			"OYB1614",
    			"QKB2189",
    			"SPL0007"};
    	this.veiculos = new ArrayList<Object[]>();
    	for(String p : placas){  
    		try{
    			veiculos.add(detranRepository.pesquisarVeiculoPlacaVistoria(p));
    		}catch(Exception e){
    			e.printStackTrace();
    		}    		
    		 try {
				Thread.sleep(5000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	try(InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/modelos/temp2.xlsx")) {
        	Context context = new Context();
            context.putVar("veiculos", veiculos);
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            JxlsHelper.getInstance().processTemplate(is,result, context);
            return new DefaultStreamedContent(new ByteArrayInputStream(result.toByteArray()), "application/xls", "vistoria.xls");
        } catch (IOException e) {
			e.printStackTrace();
		}
        return null;
    	
    }
    
    public StreamedContent getExportarExcel(){    	
    	List<ItemLeilao> itens = repository.getVeiculosDeCategoria(categoria.getId());
        try(InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/modelos/temp.xlsx")) {
        	Context context = new Context();
            context.putVar("veiculos", itens);
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            JxlsHelper.getInstance().processTemplate(is,result, context);
            return new DefaultStreamedContent(new ByteArrayInputStream(result.toByteArray()), "application/xls", categoria.getDescricao() + ".xls");
        } catch (IOException e) {
			e.printStackTrace();
		}
        return null;
    }

    @SuppressWarnings("rawtypes")
	@Override
    public Repository getRepository() {
        return this.repository;
    }

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}    
	
	public Situacao[] getSituacoes(){
		return Situacao.values();
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}    

}
