package ajaxdependancyselection


import grails.converters.JSON
import org.codehaus.groovy.grails.commons.DefaultGrailsControllerClass
import grails.web.Action
import java.lang.reflect.Method

class AutoCompleteService {
	static transactional = false
	def grailsApplication
	def autocomplete (params) {
		def domainClass = grailsApplication.getDomainClass(params.domain).clazz

		def results = domainClass.createCriteria().list {
			ilike params.searchField, params.term + '%'
			maxResults(Integer.parseInt(params.max,10))
			order(params.searchField, params.order)
		}
		if (results.size()< 5){
			results = domainClass.createCriteria().list {
					ilike params.searchField, "%${params.term}%"
					maxResults(Integer.parseInt(params.max,10))
					order(params.searchField, params.order)
			}
		}
		results = results.collect {     [label:it."${params.collectField}"] }.unique()
		return results as JSON
	}
	
	def autocompletePrimaryAction (params) {
		def domainClass = grailsApplication.getDomainClass(params.domain).clazz
		def query = {
			or{
				ilike params.searchField, params.term + '%'
			}
			projections { 
				property(params.collectField)
				property(params.searchField)
			}
			maxResults(Integer.parseInt(params.max,10))
			order(params.searchField, params.order)
		}
		def query1 = {
			or{
				ilike params.searchField, "%${params.term}%"
			}
			projections { 
				property(params.collectField)
				property(params.searchField)
			}
			maxResults(Integer.parseInt(params.max,10))
			order(params.searchField, params.order)
		}
		def results =domainClass.createCriteria().list(query)
		if (results.size()< 5){
			results = domainClass.createCriteria().list(query1)
		}
		def primarySelectList = []
		results.each {
			def primaryMap = [:]
			primaryMap.put('id', it[0])
			primaryMap.put('label', it[1])
			primarySelectList.add(primaryMap)
		}
		return primarySelectList as JSON
	}
	
	def autocompleteSecondaryAction (params) {
		def domainClass = grailsApplication.getDomainClass(params.domain).clazz
		def query = {
			eq (params.primarybind, params.primaryid.toLong())
			and{
				
				ilike params.searchField, params.term + '%'
			}
			projections { 
				property(params.collectField)
				property(params.searchField)
			}
			maxResults(Integer.parseInt(params.max,10))
			order(params.searchField, params.order)
		}
		def query1 = {
			eq (params.primarybind, params.primaryid.toLong())
			and{
				
				ilike params.searchField, "%${params.term}%"
			}
			projections { 
				property(params.collectField)
				property(params.searchField)
			}
			maxResults(Integer.parseInt(params.max,10))
			order(params.searchField, params.order)
		}
		def results =domainClass.createCriteria().list(query)
		if (results.size()< 5){
			results = domainClass.createCriteria().list(query1)
		}
		def primarySelectList = []
		results.each {
			def primaryMap = [:]
			primaryMap.put('id', it[0])
			primaryMap.put('label', it[1])
			primarySelectList.add(primaryMap)
		}
		return primarySelectList as JSON
	}
	
	def returnControllerList() {
		def clazz=grailsApplication.controllerClasses.logicalPropertyName
		def results = clazz.collect {[	'id': it, 'name': it ]}.unique()
		return results
	}
	def selectSecondary(params) {	
		if ((!params.domain2!='') && (params.domain2!=null) && (params.domain2!='null')) {
			def domainClass = grailsApplication.getDomainClass(params.domain2).clazz
			def query = {
				eq params.bindid, params.id.toLong()
				projections { 
					property(params.collectField)
					property(params.searchField)
				}
				order(params.searchField)
			}
			def results =domainClass.createCriteria().list(query)
			def primarySelectList = []
			results.each {
				def primaryMap = [:]
				//primaryMap.put(params.collectField, it[0])
				//primaryMap.put(params.searchField, it[1])
				primaryMap.put('id', it[0])
				primaryMap.put('name', it[1])
				primarySelectList.add(primaryMap)
			}
			return primarySelectList as JSON
		}
	}
	List returnPrimaryList(String className) {
		if (!className.equals('')) {
			Class clazz = grailsApplication.getDomainClass(className).clazz
			clazz.list()
		}
	}
	def returnControllerActions(params) {
		if ((!params.id!='') && (params.id!=null) && (params.id!='null')) {
			String s = params.id
			String domclass1= (s.substring(0,1).toUpperCase())
			String domclass2=s.substring(1,s.length())
			String domclass=domclass1+domclass2
			def fp=grailsApplication.metadata['app.name']
			def gp=fp+"."+domclass
			Class clazz = grailsApplication.getDomainClass(fp+"."+domclass).clazz
			List<String> list=new ArrayList<String>();
			grailsApplication.controllerClasses.each { DefaultGrailsControllerClass controller ->
				Class controllerClass = controller.clazz
				if (controllerClass.name.startsWith(gp.toString())) {
					String logicalControllerName = controller.logicalPropertyName
					controllerClass.methods.each { Method method ->
						if (method.getAnnotation(Action)) {
							list.add(method.name)
						}
					}
				}
			}
			def results = list.collect {	['id':it,'name':it] }.unique()
			return results as JSON
		}
	}
	
}
