package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.servlet.ModelAndView;


import exception.AdminRequiredException;
import exception.LoginRequiredException;
import exception.MailEmptyException;
import logic.Item;
import logic.Mail;
import logic.Sale;
import logic.SaleItem;
import logic.ShopService;
import logic.User;

@Controller
public class UserController {
	private String naverid ="rladnd9927@naver.com";
	private String naverpw ="wkrkdyd1";
	@Autowired
	private ShopService shopService;
	@RequestMapping("user/userEntryForm")
	public ModelAndView userEntryForm(){
		ModelAndView mav = new ModelAndView("user/userEntry");
		User user= new User();
		DateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			user.setBirthDay(sf.parse("1990-01-01"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		mav.addObject(user);
		return mav;
	}
	
	@RequestMapping("user/userEntry")
	public ModelAndView userSubmit(@Valid User user, BindingResult bindingResult)throws Exception{
		ModelAndView mav = new ModelAndView();
		if(bindingResult.hasErrors()){
			bindingResult.reject("error.input.user");
			mav.getModel().putAll(bindingResult.getModel());
			return mav;
		}
		
		try{
			shopService.createUser(user);
			mav.setViewName("user/login");
			mav.addObject("user",user);
		}catch(DataIntegrityViolationException e){
			bindingResult.reject("error.duplicate.user");
		}
		return mav;
	}
	
	@RequestMapping("user/loginForm")
	public ModelAndView loginForm(){
		ModelAndView mav = new ModelAndView("user/login");
		
		mav.addObject(new User());
		return mav;
	}
	
	@RequestMapping("user/login")
	public ModelAndView login(@Valid User user, BindingResult bindingResult,HttpSession session){
		ModelAndView mav = new ModelAndView();
		if(bindingResult.hasErrors()){
			mav.getModel().putAll(bindingResult.getModel());
			return mav;
		}
		try{
			User loginUser = shopService.getUserByIdPw(user.getUserId(), user.getPassword());
			session.setAttribute("USER", loginUser);
			List<Sale> salelist=shopService.saleList(user.getUserId());
			//salelist: user�� �ֹ��� ���, sale���̺��� ��ȸ�Ѵ�.
			for(Sale sale : salelist){
				//saleItemList : saleId�� ���� �ֹ� ��ǰ�� ����� ����Ǿ� ����.
				List<SaleItem> saleItemList = shopService.saleItemList(sale.getSaleId());
				for(SaleItem sitem : saleItemList){
					Item item = shopService.getItemById(sitem.getItemId());
					sitem.setItem(item);
				}
				sale.setSaleItemList(saleItemList);
			}
			
			mav.setViewName("user/mypage");
			mav.addObject("user",loginUser);
			mav.addObject("salelist",salelist);
			return mav;
		}catch(EmptyResultDataAccessException e){
			bindingResult.reject("error.login.id");
			mav.getModel().putAll(bindingResult.getModel());
		}
		return mav;
	}
	
	@RequestMapping("user/mypage")
	public ModelAndView mypage(String id){
		ModelAndView mav = new ModelAndView();
		User user = shopService.getUserById(id);
		List<Sale> salelist=shopService.saleList(id);
		for(Sale sale : salelist){
			List<SaleItem> saleItemList = shopService.saleItemList(sale.getSaleId());
			for(SaleItem sitem : saleItemList){
				Item item = shopService.getItemById(sitem.getItemId());
				sitem.setItem(item);
			}
			sale.setSaleItemList(saleItemList);
		}
		mav.addObject("salelist",salelist);
		mav.addObject("user",user);
		return mav;
	}
	@RequestMapping("user/logout")
	public ModelAndView logout(HttpSession session){
		session.invalidate();
		return loginForm();
	}
	@RequestMapping("user/admin")
	public ModelAndView admin(HttpSession session){
		User loginUser = (User)session.getAttribute("USER");
		if(loginUser == null){
			throw new LoginRequiredException("�α����� �ʿ��մϴ�.");
			
		}
		if(!loginUser.getUserId().equals("admin")){
			throw new AdminRequiredException("�����ڸ� �����մϴ�.");
		}
		
		ModelAndView mav = new ModelAndView();
		List<User> userList = shopService.userList();
		mav.addObject("userList",userList);
		return mav;
	}
	@RequestMapping("user/mailForm")
	public ModelAndView mailForm(String[] idchks){ //idchks�� ���� �Ѿ�� userId���� ���ڿ� �迭�� ����
		ModelAndView mav = new ModelAndView();
		
		if(idchks == null || idchks.length == 0){
			throw new MailEmptyException("ȸ���� ���� �ϼ���.");
		}
		List<User> userList = shopService.userList(idchks);//���񽺸� ���� userDaoImpl�� userList�� ȣ��ȴ�. (userDaoImpl.java ����)
		//userList�������� ���õ� ID�� �������� User��ü�� ����ְ�, �� User��ü���� List���·� ������ �ִ�.
		
		
		mav.addObject("userList",userList); //View������ �Ѱ��ش�.(mailForm.jspȮ��)
		return mav;
	}
	@RequestMapping("user/mail")
	public ModelAndView mail(Mail mail){
		
		ModelAndView mav = new ModelAndView("user/mailsuccess");
		adminMailSend(mail);
		return mav;
	}
	private void adminMailSend(Mail mail) {
		MyAuthenticator auth = new MyAuthenticator(naverid,naverpw);//���̹� ������ �̿��Ϸ��� '����'�۾��� �ʿ���.
		Properties prop = new Properties();
		FileInputStream fis;
		try{
			File pr = new File("C:/mywork/spring/workspace/shop-3/mail.properties"); //mail.properties�� ���ϼ����� �̿��ϴ� ���� ���������� �� ����.
			fis = new FileInputStream(pr);
			prop.load(fis); //�� ȯ������ prop�� mail.properties�� �ִ� ������ ���� �������Ե�
		}catch(IOException e){
			e.printStackTrace();
		}
		Session session = Session.getInstance(prop,auth); //mail������ ����Ϸ� (prop�� �ִ� ���ϼ��� ������ ���̹� ������ �����Ѵ�. �̶�, ������ü auth�� �ݵ�� �ʿ��ϴ�. (��κ��� ���� ���񽺵��� ���ȸ��Ϸ� �ٲ�� ���� ���������� �ʼ�����)
		MimeMessage msg = new MimeMessage(session); //msg : �޴� ������� ������ ������ ������ �߰��� ����.
		
		try{
			List<InternetAddress> addrs = new ArrayList<InternetAddress>(); //������ (�޴� ��)�� �ּҼ����� ����Ʈ ���·� ��(������ �������)
			msg.setFrom(new InternetAddress(naverid)); //�۽��� (������ ��)�� ���� �ּҼ����� ��. (InternetAddress�� ���̹� ���̵� �־�, �ּҸ� ��������)
			String[] emails = mail.getRecipient().split(",");//���� �޴��̵��� �ּҵ��� , �� �������� �������� �����Ƿ� ,�� �������� split()��
			for(int i = 0 ; i<emails.length;i++){
				try{
					addrs.add(new InternetAddress(new String(emails[i].getBytes("euc-kr"),"8859_1"))); //�츮�� ���������� euc-kr ���ڵ��� ����ϰ� �����Ƿ�, ���۽ÿ� 8859_1 ���·� �ٲپ� ��� �ѱ��� �ȱ���
				}catch(UnsupportedEncodingException e){
					e.printStackTrace();
				}
			}
			InternetAddress[] arr= new InternetAddress[emails.length];//�޴��̵��� �� ��ŭ arr �迭�� ���� -> �� �迭���� �����ڵ��� �̸��� �ּҸ� ������ ����
			
			for(int i =0 ; i<addrs.size();i++){
				arr[i] = addrs.get(i);
			}
			msg.setSentDate(new Date()); //������ �ð��� msg�� ����
			InternetAddress from = new InternetAddress(naverid); 
			msg.setFrom(from);
			msg.setRecipients(Message.RecipientType.TO, arr); //�����ڿ� ���� ���� (Message.RecipientType.TO : �޴� ���,    Message.RecipientType.CC : ������
			msg.setSubject(mail.getTitle());
			msg.setContent(mail.getContents(), mail.getMtype()); //������� msg�� ����, ������� �����Ѵ�.
			MimeMultipart multipart = new MimeMultipart(); //������� ÷�������� �����ϴ� �κ�
			MimeBodyPart message = new MimeBodyPart();
			
			message.setContent(mail.getContents(),mail.getMtype()); 
			multipart.addBodyPart(message);//'����' �κ��� ���Ͽ� �߰�. (÷�������� ���� �����Ե� ����. ������ �⺻������ �ٵ���Ʈ�� ������ ������ ��)
			if(mail.getFile1()!=null && !mail.getFile1().isEmpty()){
				multipart.addBodyPart(bodyPart(mail));//÷�������� '����'�� �߰���.
			}
			msg.setContent(multipart); //÷�������� �߰�
			Transport.send(msg); //������ ����
		}catch(MessagingException e){
			e.printStackTrace();
		}
	}

	   private BodyPart bodyPart(Mail mail) {
		      MimeBodyPart body = new MimeBodyPart();
		      FileOutputStream fos = null;
		      try{
		         String filename = "c:/shop-3/mail/" + mail.getFile1().getOriginalFilename(); //���� ���ε��ϴ� ������ �ϳ� ����.
		         File f1 = new File(filename); //���� ������ ��η� ���� ��ü�ϳ� ���� (���� �������)
		         fos = new FileOutputStream(f1); //���ε�� ������ ����
		         InputStream in = mail.getFile1().getInputStream();
		         int data;
		         while((data=in.read()) != -1){
		            fos.write(data); //"c:/shop-3/mail/�����̸�" ���� ������ ����.
		         } //������� �ܼ��� '����'�� �����ϴ� ����
		         body.attachFile(f1); // �ٵ���Ʈ�� �� ������ �ƴ´�.
		         body.setFileName(new String(f1.getName().getBytes("euc-kr"),"8859_1")); //������ �̸��� "���/�����̸�"���� ���� �����̸����� �ٲ��� (f1.getName())
		      }catch(Exception e){
		         e.printStackTrace();
		      }finally{
		         try{
		            if(fos != null){
		               fos.flush();
		               fos.close();
		            }
		         }catch(IOException e){}
		      }
		      return body; //������ ����ִ� body�� ������
		   }

	
	public final class MyAuthenticator extends Authenticator{
		private String id;
		private String pw;
		public MyAuthenticator(String id, String pw){
			this.id=id;
			this.pw=pw;
		}
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(id,pw);
		}
		
	}
	/* birthday������, DateŸ���̾ Ÿ���� �ȸ¾�, ��� �ٷ� �������� ����!! -> @InitBinder�� �̿��Ѵ�
	 * 
	 * InitBinder�� ���� - �Է��׸� ����(������ ������) + �� ��ȯ���   => �ؽ�Ʈâ�� ���� birthDay�� ������ String������ Date������ üũ�ϰ�, Date�� �ƴѰ�� ����ȯ ���� ���ش�.
	 * ���࿡ ������ ���� �߻��ϸ� ? -> typeMismatch.'�Ķ�����̸�' ���� ���� �Ѿ (WebDataBinder Ŭ������ registerCustomEditor()�޼ҵ尡 �� ������ ��)
	 * 
	 */
	@InitBinder //������ ������ �Ѿ�ö� @InitBinder������̼��� �ִٸ� @RequestMapping���ٵ� ���� �����Ͽ�, �����͵��� ���ε����� RequestMapping�� User��ü�� �Ѱ��ְԵ�)
	public void initBinder(WebDataBinder binder) throws Exception{
		DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
		
		//Date.class : �Ķ���� ���� Date������ �����ϰڴ�.
		//"birthDay" : �������� �Ǵ� �Ķ������ �̸�. (birthDay��� ������Ƽ�� Date������ ��ȯ��Ű�ڴ�.) => �� �κ� ������ Ư�� ������Ƽ�� ���ؼ��� ��ȯ�ϴ� ���̾ƴ϶� ��� DateŸ���� ������Ƽ�� �����Ѵٴ� ��
		//new CustomDateEditor(dfm, true)
		//		- true : ���ڿ��� ��� 
		//		- false : ���ڿ��� ����� (��������� �ݵ��! �ʿ��Ѱ��, false�� ����)
		binder.registerCustomEditor(Date.class,new CustomDateEditor(dfm, true)); //true : ��¥ �κ��� �� ����ϰڴ�.
	}
}
