pre{
	"CompareHospital is started . . ".println();
}

rule Mod
	match b : BaseModel!Model with l : In!Model {
		compare : true	
}

rule Pack
	match b : BaseModel!Package with l : In!Package {
		compare : true	
}


rule Class
	match b : BaseModel!Class with l : In!Class{

		compare : b.name = l.name
		and
					b.isAbstract=l.isAbstract
}

rule Property
	match b : BaseModel!Property with l : In!Property{

		compare : 
		b.name = l.name and b.owner.name = l.owner.name

}

rule Association
	match b : BaseModel!Association with l : In!Association{

		compare : 
		b.name = l.name 

}

post{
	"CompareHospital is ended . . ".println();
}