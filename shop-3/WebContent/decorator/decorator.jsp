<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title><decorator:title/></title>
<decorator:head/>
</head>
<body>
	<table border="1" align="center" cellspacing="0" cellpadding="0" width="100%">
			<tr>
				<td colspan="3" align="right">
					<c:if test="${empty USER }">
							<a href = "${path }/user/loginForm.shop">�α���</a>
					</c:if>
					<c:if test="${not empty USER }">
						<c:if test="${USER.userId=='admin' }">
							<a href = "${path }/user/admin.shop">������������</a>
							<a href ="${path }/user/logout.shop">�α׾ƿ�</a>
						</c:if>
						<c:if test="${USER.userId!='admin' }">
							ȯ���մϴ�. ${USER.userName }��
							<a href="${path }/user/mypage.shop?id=${USER.userId }">
								����������
							</a>
							<a href ="${path }/user/logout.shop">�α׾ƿ�</a>
						</c:if>
					</c:if>
				</td>
			</tr>
			<tr>
				<td width="15%" align="right" valign="top"><br>
					<a href = "${path }/item/list.shop">��ǰ����</a><br>
					<a href = "${path }/board/list.shop">�Խ���</a><br>
					<a href = "${path }/cart/cartView.shop">��ٱ���</a><br>
				</td>
				<td colspan="2">
					<decorator:body/>
				</td>
			</tr>
			<tr>
				<td colspan="3" align="center">
					Spring ,. Made by woong in 2016
				</td>
			</tr>
	</table>
</body>
</html>