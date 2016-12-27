<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    <%@include file = "/WEB-INF/jsp/jspHeader.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>����� ��� ȭ��</title>
</head>
<body>

<%--����--
	../shop-3/user/userEntryForm.shop���� ��û��(URL), �� �������� ��� �ǵ��� Controller�� �޼ҵ���� �ۼ��ϱ�.
 --%>

<h2>����� ���</h2>

	<%--��modelAttribute �Ӽ� : �� ����� ���޵Ǵ� ��ü�� �����Ѵ�.
		�� �������� ����Ǹ� "user"��� ��ü�� �Ѱܹޱ� ��ٸ��� �ִµ�, UserEntryFormControllerŬ������ @RequestMapping�κ��� ���� user ��ü��
		�Ѱ����� �ʴ´�. �̶�, ��ü�� �����Ƿ� �������� @ModelAttribute�� �ִ� �޼ҵ忡�� user��ü�� �Ѱ��ְ� �ȴ�.
	 --%>
<form:form modelAttribute="user" method="post" action="userEntry.shop"> <%--���� ��ư�� ������ �� �������� ȣ���Ҷ��� ���� userEntry.shop���� ��û�� �����µ�, method����� post�̴�. UserEntryController���� POST����϶� �ʿ��� @RequestMapping�� ���������Ѵ�. --%>
	<spring:hasBindErrors name="user">	<%--shop-2���� validatorŬ�������� reject�޼ҵ带 �̿�������, ������̼��� �̿��ؼ� ó�� ������. --%>
		<font color="red">
			<c:forEach items= "${errors.globalErrors}" var="error"><%--erros.globalErrors�� UserEntryValidator.java Ŭ������ reject("error.input.users")�� �ش��Ѵ�. (reject()�� messages.properties�� error.input.users ���� Ȯ�ΰ���)  --%>
				<spring:message code ="${error.code}"/>
			</c:forEach>	
		</font>
	</spring:hasBindErrors>
	<table>
		<tr>
			<td>���̵�</td>
			<td><form:input path="userId"/>
				<font color="red">
				<form:errors path="userId"/></font><%--form:errors�� UserEntryValidator.java Ŭ������ rejectValue("userId","error.required")�� �ش� --%>
			</td>
		</tr>
		<tr>
			<td>��й�ȣ</td>
			<td><form:input path="password"/>
				<font color="red">
				<form:errors path="password"/></font>
			</td>
		</tr>
		<tr>
			<td>�̸�</td>
			<td><form:input path="userName"/>
				<font color="red">
				<form:errors path="userName"/></font>
			</td>
		</tr>
		<tr>
			<td>��ȭ��ȣ</td>
			<td><form:input path="phoneNo"/>
				<font color="red">
				<form:errors path="phoneNo"/></font>
			</td>
		</tr>
		<tr>
			<td>�����ȣ</td>
			<td><form:input path="postCode"/>
				<font color="red">
				<form:errors path="postCode"/></font>
			</td>
		</tr>
		<tr>
			<td>�ּ�</td>
			<td><form:input path="address"/>
				<font color="red">
				<form:errors path="address"/></font>
			</td>
		</tr>
		<tr>
			<td>�̸���</td>
			<td><form:input path="email"/>
				<font color="red">
				<form:errors path="email"/></font>
			</td>
		</tr>
		<tr>
			<td>�������</td>
			<td><form:input path="birthDay"/>
				<font color="red">
				<form:errors path="birthDay"/></font>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<input type= "submit" value="���"/>
				<input type= "reset" value="�ʱ�ȭ"/>
			</td>
		</tr>
	</table>
</form:form>
</body>
</html>