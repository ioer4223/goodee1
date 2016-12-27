package controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import exception.CartEmptyException;
import exception.LoginRequiredException;
import logic.Cart;
import logic.Sale;
import logic.ShopService;
import logic.User;

@Controller
public class CheckoutController {

	@Autowired
	private ShopService shopService;
	
	@RequestMapping("checkout/checkout")
	public ModelAndView checkout(HttpSession session){
		//�α��� ���°� �ƴѰ�� ���� ó���ϱ�
		User loginUser = (User)session.getAttribute("USER");
		if(loginUser == null){
			throw new LoginRequiredException("�α����� �ʿ��� �����Դϴ�.");
		}
		
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart == null || cart.isEmpty()){
			throw new CartEmptyException("īƮ�� ��ǰ�� �����ϴ�.");
		}
		
		ModelAndView mav = new ModelAndView();
		Integer totalAmount=shopService.calculateTotalAmount(cart.getItemList());  //cart.jsp �信�� ��ǰ����Ʈ ����ٶ�ó�� varStatus�̿��ؼ� ����Ʈ ����ص��ǰ� ����ó�� ��Ʈ�ѷ� �ܿ��� ����(�Ϻη� �ٸ�������� �ۼ�)
		mav.addObject("itemList",cart.getItemList());
		mav.addObject("totalAmount",totalAmount);
		mav.addObject("loginUser",loginUser);
		return mav;
	}
	
	@RequestMapping("checkout/end")
	public ModelAndView end(HttpSession session){
		Cart cart = (Cart)session.getAttribute("CART");
		User loginUser = (User)session.getAttribute("USER");
		if(cart == null || cart.isEmpty()){
			throw new CartEmptyException("");
		}
		ModelAndView mav = new ModelAndView();
		
		Sale sale = shopService.checkEnd(loginUser,cart);
		int totalAmount = shopService.calculateTotalAmount(cart.getItemList());
		mav.addObject("itemList",cart.getItemList());
		cart.clearAll(session);
		mav.addObject("totalAmount",totalAmount);
		mav.addObject("loginUser",loginUser);
		mav.addObject("sale",sale);
		return mav;
	}
}
