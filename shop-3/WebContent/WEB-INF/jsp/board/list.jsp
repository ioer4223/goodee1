<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    <%@ include file = "/WEB-INF/jsp/jspHeader.jsp" %>
    
    <%--BoardController list()���� ���� ����� �����ϴºκ� (serViewName)�� ���⶧���� requestmapping�� �Ѿ�� ��ο� ���� �ٷ� list.jsp�� ���� --%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>�Խ���</title>
</head>
<body>

<table border="1" cellspacing="0" cellpadding="0" width="100%">
	<c:if test="${listcount>0 }">
		<tr>
			<td colspan="4" align="center">��ǰ�ǰ� �Խ���</td>
			<td>�� ����: ${listcount }</td>
		</tr>
		<tr align="center" valign="middle" bordercolor="#212121">
			<td width="8%">
				��ȣ
			</td>
			<td width="50%">
				����
			</td>
			<td width="14%">
				�ۼ���
			</td>
			<td width="17%">
				��¥
			</td>
			<td width="11%">
				��ȸ��
			</td>
		</tr>
		<c:set var="boardnum" value="${boardnum }"/>
		<c:forEach items="${boardlist }" var = "board">
			<tr align="center" valign="middle" bordercolor="#333333" onmouseover="this.style.backgroundColor='#5CD1E5'" onmouseout="this.style.backgroundColor=''">
				<td>${boardnum}</td>
				<c:set var="boardnum" value="${boardnum-1 }"/>
				<td align="left">
					<c:if test="${not empty board.fileUrl }">
						<a href="../fileupload/${board.fileUrl}">@</a>
					</c:if>
					<c:if test="${empty board.fileUrl }">
						&nbsp;&nbsp;&nbsp;
					</c:if>
					<c:forEach begin="1" end="${board.refLevel }">
						&nbsp;&nbsp;&nbsp;
					</c:forEach>
					<c:if test="${board.refLevel>0 }">
						��
					</c:if>
					
					<a href="detail.shop?num=${board.num }">${board.subject}</a>
				</td>
				<td align="left">
					${board.name }
				</td>
				<td align="center">
					<c:if test="${board.dateFormat == today }">
						<f:formatDate value="${board.regDate}" pattern="HH : mm : ss"/>
					</c:if>
					<c:if test="${board.dateFormat != today }">
						<f:formatDate value="${board.regDate}" pattern="yyyy-MM-dd HH : mm"/>
					</c:if>
				</td>
				<td align="center">
					${board.readCnt }
				</td>
			</tr>
		</c:forEach>
		<tr align="center">
			<td colspan="5">
				<c:if test="${pageNum >1 }">
					<a href = "list.shop?pageNum=${pageNum-1 }">[����]</a>
				</c:if>
				<c:if test="${pageNum <=1 }">
					[����]
				</c:if>
				<c:forEach var="a" begin="${startpage }" end="${endpage}">
					<c:if test="${pageNum==a }">[${a}]</c:if>
					<c:if test="${pageNum!=a }"><a href ="list.shop?pageNum=${a}">[${a}]</a></c:if>
				</c:forEach>
				<c:if test="${pageNum<maxpage}">
					<a href = "list.shop?pageNum=${pageNum+1}">[����]</a>
				</c:if>
				<c:if test="${pageNum>=maxpage}">
					[����]
				</c:if>
			</td>
		</tr>
	</c:if>
	<c:if test="${listcount==0 }">
		<tr>
			<td colspan="5">
				��ϵ� �Խù��� �����ϴ�.
			</td>
		</tr>
	</c:if>
	<tr>
		<td align="right" colspan="5">
			<a href="add.shop">[�۾���]</a>
		</td>
	</tr>
</table>

</body>
</html>