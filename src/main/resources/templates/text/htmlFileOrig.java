	<div id="edit" th:fragment="edit">
		<form id="submitForm" action="" th:action="@{/[(${objectName})]}" method="POST" th:object="${item}">
		<table name="submitForm"  class="table table-bordered" align="center">
			<tbody>
				<!-- Netex Id -->
				<div th:replace="/html/simpleInput :: input('[(${objectName})].id','item.[(${objectName})].id','Id','true')"></div>
				<!-- Name -->
				<div th:replace="/html/inputMulti :: inputMulti('[(${objectName})].name.value','item.[(${objectName})].name.value','[(${objectName})].name.lang','Name','true')"></div>
				<!-- ShortName -->
				<div th:replace="/html/inputMulti :: inputMulti('[(${objectName})].shortName.value','item.[(${objectName})].shortName.value','[(${objectName})].shortName.lang','Short Name','false')"></div>
				<!-- version -->
				<div th:replace="/html/simpleInput :: input('[(${objectName})].version','item.[(${objectName})].version','Version','true')"></div>
				<!-- PrivateCode -->
				<div th:replace="/html/privateCode :: privateCode(	'[(${objectName})].privateCode.value',
																	'item.[(${objectName})].privateCode.value',
																	'[(${objectName})].privateCode.type',
																	'item.[(${objectName})].privateCode.type',
																	'Private code','true')"></div>
				<!-- hasPhoto 
				<div th:replace="/html/boolean :: boolean('[(${objectName})].hasPhoto','Has Photo')"></div>
				-->
				
				<!-- Description -->
				<div th:replace="/html/textareaMulti :: textareaMulti('[(${objectName})].description.value','item.[(${objectName})].description.value','Description','[(${objectName})].description.lang')"></div>				
				
				<!-- Operator -->
				<div th:replace="/html/selectOption :: option('operator','Operator name','item.operator','/BaseInfo')"/>
								
				<tr>
					<td><input type="submit" value="Submit" /></td>
				</tr>
			</tbody>
		</table>
		</form>
		
		
	</div>