package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import exception.CartEmptyException;
import logic.Cart;
import logic.Item;
import logic.ItemSet;
import logic.ShopService;

@Controller
public class CartController {

	@Autowired
	private ShopService shopService;
	
	@RequestMapping("cart/cartAdd") //id = ��ǰ ���̵�, quantity = ����
	public ModelAndView add(Integer id, Integer quantity, HttpSession session){ //�ش� ������Ʈ���� �޼ҵ忡�� request��ü�� ����ϰ������ HttpServletRequest, session��ü�� ����ϰ� ������ HttpSession �� �Ű������� �޴´�. 
																	//���⼱ ���ǰ�ü�� cart�� ����ϱ����� ���
		Item selectedItem = shopService.getItemById(id); //selectedItem : ���õ� ��ǰ����
		Cart cart = (Cart)session.getAttribute("CART");
		
		if(cart == null){
			cart=shopService.getCart(); //�� ������ īƮ�� ����
			session.setAttribute("CART", cart); //īƮ�� "����"�� ������ �س��� , cart��� �Ӽ��� ������� �������(null) �� ������ īƮ�� �ϳ� ���� ���ǿ� ���
		}
		
		cart.push(new ItemSet(selectedItem, quantity));
		ModelAndView mav = new ModelAndView("cart/cart");
		
		mav.addObject("message", selectedItem.getName() + quantity +"���� īƮ�� �߰��Ͽ����ϴ�.");
		mav.addObject("cart",cart);
		
		return mav;
	}
	@RequestMapping("cart/cartDelete")
	public ModelAndView delete(int index, HttpSession session){
		Cart cart = (Cart)session.getAttribute("CART"); //���ǿ��� CART ��Ʈ����Ʈ�� �մ� īƮ��ü�� cart ������ ��´�.(��ü, �ε��� �߳Ѿ��)
		String name = cart.remove(index);

		ModelAndView mav = new ModelAndView("/cart/cart");
		mav.addObject("message", name+" ��ǰ�� ��ٱ��Ͽ��� �����Ǿ����ϴ�.");
		mav.addObject("cart",cart);
		return mav;
	}
	@RequestMapping("cart/cartView")
	public ModelAndView cartView(HttpSession session){
		Cart cart = (Cart)session.getAttribute("CART");
		
		if(cart==null || cart.isEmpty()){
			throw new CartEmptyException("��ٱ��Ͽ� ��ǰ�� �����ϴ�.");
		}
		ModelAndView mav = new ModelAndView("cart/cart");
		mav.addObject("message","��ٱ��� ���� ȭ�� �Դϴ�.");
		mav.addObject("cart",cart);
		return mav;
	}
}
