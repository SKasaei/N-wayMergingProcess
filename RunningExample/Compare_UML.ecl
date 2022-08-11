pre{
	"CompareSimpleOO is started (1) . . ".println();
}

rule Pack
	match b : BaseModel!Package with l : In!Package {
		compare : true	
}


rule Class
	match b : BaseModel!Class with l : In!Class{

		compare : b.name = l.name
		
}

rule Property
	match b : BaseModel!Property with l : In!Property{

	compare : b.name = l.name and b.owner.name = l.owner.name

}

rule Operation
	match b : BaseModel!Operation with l : In!Operation{

		compare : b.name = l.name

}

post{
	"CompareSimpleOO is ended (1) . . ".println();
}