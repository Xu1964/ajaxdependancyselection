
<div class="triggerFilters-${attrs.id}">

<label>Enable Filter?</label> 
<input type="checkbox" name="${attrs.id}check" id="${attrs.id}TriggerCheck"   onChange="${attrs.id}TriggerFilter(this)" />
</div>
<g:javascript>
function ${attrs.id}TriggerFilter(e) {
	if (e.checked==true) {
		$.get('<g:createLink action='${attrs.filteraction}' controller="${attrs.controller}" params="[ filterbind: ''+attrs.filterbind+'', id: ''+attrs.id+'', prevValue: ''+prevValue+'', prevId: ''+attrs.prevId+'', domain2: ''+attrs.domain2+'', bindid: ''+attrs.bindid+'', hidden:''+attrs.hidden+'',acontroller: ''+attrs.controller+'', filteraction2: ''+attrs.filteraction2+'', domain: ''+attrs.domain+'', searchField: ''+attrs.searchField+'', max: ''+attrs.max+'', order: ''+attrs.order+'', collectField: ''+attrs.collectField+'' ,appendValue: ''+attrs.appendValue+'' , appendName: ''+attrs.appendName+'' ]"/>',function(data){
			$('#${attrs.id}FilterField').hide().html(data).fadeIn('slow');
		});
	}else{
		$('#${attrs.id}FilterField').hide().html('').fadeIn('slow');
	}
}
</g:javascript>

<div id="${attrs.id}FilterField" class="filterField-${attrs.id}">

</div>
