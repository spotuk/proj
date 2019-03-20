<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>


<html>
<c:set var="title" value="cart" />
<%@ include file="/WEB-INF/jspf/head.jspf"%>

<body>
	<%-- HEADER --%>
	<%@ include file="/WEB-INF/jspf/header.jspf"%>
	<%-- HEADER --%>
	<p>
		<font face="Verdana, Arial, Helvetica, sans-serif"><strong>Shopping
				Cart</strong></font>
	</p>
	<table width="75%" border="1">
		<tr bgcolor="#CCCCCC">
			<td><strong><font size="2"
					face="Verdana, Arial, Helvetica, sans-serif">Model
						Description</font></strong></td>
			<td><strong><font size="2"
					face="Verdana, Arial, Helvetica, sans-serif">Quantity</font></strong></td>
			<td><strong><font size="2"
					face="Verdana, Arial, Helvetica, sans-serif">Unit Price</font></strong></td>
			<td><strong><font size="2"
					face="Verdana, Arial, Helvetica, sans-serif">Total</font></strong></td>
		</tr>
		<jsp:useBean id="cart" scope="session"
			class="ua.kpi.getman.InternetShop.bean.CartBean" />
		<c:if test="${cart.lineItemCount==0}">
			<tr>
				<td colspan="4"><font size="2"
					face="Verdana, Arial, Helvetica, sans-serif">- Cart is
						currently empty -<br />
			</tr>
		</c:if>
		<c:forEach var="cartItem" items="${cart.cartItems}"
			varStatus="counter">
			<form name="item" method="POST" action="controller">
				<tr>
					<td><font size="2"
						face="Verdana, Arial, Helvetica, sans-serif"><b><c:out
									value="${cartItem.partNumber}" /></b><br /> <c:out
								value="${cartItem.modelDescription}" /></font></td>
					<td><font size="2"
						face="Verdana, Arial, Helvetica, sans-serif"><input
							type='hidden' name='itemIndex'
							value='<c:out value="${counter.count}"/>'><input
							type='text' name="quantity"
							value='<c:out value="${cartItem.quantity}"/>' size='2'> <input
							type="hidden" name="command" value="cart" /> <input
							type="submit" name="action" value="Update"> <br /> <input
							type="submit" name="action" value="Delete"></font></td>
					<td><c:out value="${cartItem.unitCost}" /> <font size="2"
						face="Verdana, Arial, Helvetica, sans-serif">uah</font></td>
					<td><c:out value="${cartItem.totalCost}" /> <font size="2"
						face="Verdana, Arial, Helvetica, sans-serif">uah</font></td>
				</tr>
			</form>

		</c:forEach>
		<tr>
			<td colspan="2"></td>
			<td></td>
			<td><font size="2" face="Verdana, Arial, Helvetica, sans-serif">Subtotal:
					<c:out value="${cart.orderTotal}" /> &ensp; uah
			</font></td>
		</tr>

	</table>
	<c:if test="${not empty userRole}">
		<form action="controller" method="post"
			xmlns="http://www.w3.org/1999/html">
			<input type="hidden" name="command" value="order" />
			<input type="hidden" name="action" value="addOrder" />
			<input type="hidden" name="user_id" value="${user.id}" />

				<button type="submit" class="btn btn-outline-warning">Order</button>
		</form>
	</c:if>
</body>
</html>