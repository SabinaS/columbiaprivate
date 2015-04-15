import java.util.ArrayList;
import java.util.HashMap;

import org.opencv.core.Core;


public class WhereDescriptions {

	public void run(Building[] buildings){
		HashMap<Integer, ArrayList<int[]>> buildingDescriptions = new HashMap<>(); 
		Building b1 = new Building(28, 1);
		b1.setCenterOfMassX(222);
		b1.setCenterOfMassY(152);
		Building b2 = new Building(29, 1);
		b2.setCenterOfMassX(173);
		b2.setCenterOfMassY(272);
		Building b3 = new Building(30, 1); 
		b3.setCenterOfMassX(59);
		b3.setCenterOfMassY(60);
		Building b4 = new Building(31, 1);
		b4.setCenterOfMassX(144);
		b4.setCenterOfMassY(229);
		Building b5 = new Building(32, 1);
		b5.setCenterOfMassX(71);
		b5.setCenterOfMassY(12);
		Building b6 = new Building(33, 1);
		b6.setCenterOfMassX(111);
		b6.setCenterOfMassY(466);
		//buildingDescriptions = compareBuildings(buildingDescriptions, buildings); 
		buildingDescriptions = compareOneBuilding(buildingDescriptions, buildings, b6); 
		buildingDescriptions = reduceDescriptions(buildingDescriptions, buildings, b6); 
		
		//Test
		/*for(int d = 1; d < 28; d++){
			Building b = buildings[d]; 
			ArrayList<int[]> descr = buildingDescriptions.get(d); 
			//System.out.println("Building Number: " + b.getBuildingNumber());
			for(int a = 0; a < descr.size(); a++){
				if(descr.get(a)[0]==0){
					System.out.println("Building " + descr.get(a)[1] + " is near Building" + descr.get(a)[2]); 
				}else if(descr.get(a)[0]==1){
					System.out.println("Building " + descr.get(a)[1] + " is north of Building" + descr.get(a)[2]); 
				}else if(descr.get(a)[0]==2){
					System.out.println("Building " + descr.get(a)[1] + " is south of Building" + descr.get(a)[2]); 
				}else if(descr.get(a)[0]==3){
					System.out.println("Building " + descr.get(a)[1] + " is east of Building" + descr.get(a)[2]); 
				}else if(descr.get(a)[0]==1){
					System.out.println("Building " + descr.get(a)[1] + " is west of Building" + descr.get(a)[2]); 
				}
			}
			System.out.println(""); 
		}*/
		
		//Test One
			ArrayList<int[]> descr = buildingDescriptions.get(b1.getBuildingNumber()); 
			//System.out.println("Building Number: " + b.getBuildingNumber());
			System.out.println("size " + descr.size()); 
			for(int a = 0; a < descr.size(); a++){
				if(descr.get(a)[0]==0){
					System.out.println("Building " + descr.get(a)[1] + " is near Building " + descr.get(a)[2]); 
				}else if(descr.get(a)[0]==1){
					System.out.println("Building " + descr.get(a)[1] + " is north of Building " + descr.get(a)[2]); 
				}else if(descr.get(a)[0]==2){
					System.out.println("Building " + descr.get(a)[1] + " is south of Building " + descr.get(a)[2]); 
				}else if(descr.get(a)[0]==3){
					System.out.println("Building " + descr.get(a)[1] + " is east of Building " + descr.get(a)[2]); 
				}else if(descr.get(a)[0]==1){
					System.out.println("Building " + descr.get(a)[1] + " is west of Building " + descr.get(a)[2]); 
				}
			}

	}
	
	public HashMap<Integer, ArrayList<int[]>> compareOneBuilding(HashMap<Integer, ArrayList<int[]>> descriptions, Building[] buildings, Building b){
		HashMap<Integer, ArrayList<int[]>> buildingDescriptions = descriptions; 
		
			ArrayList<int[]> descr = new ArrayList<int[]>();
			for(int j = 1; j < buildings.length; j++){
			
					if(isNear(b, buildings[j])){
						//System.out.println("Building " + b.getBuildingNumber() + " is near Building " + buildings[j].getBuildingNumber()); 
						int[] near = {0, b.getBuildingNumber(), buildings[j].getBuildingNumber()}; 
						descr.add(near); 
					}
					if(isNorth(b, buildings[j])){
						int[] north = {1, b.getBuildingNumber(), buildings[j].getBuildingNumber()}; 
						//System.out.println("Building " + b.getBuildingNumber() + " is north of Building " + buildings[j].getBuildingNumber()); 
						descr.add(north); 
					}
					if(isSouth(b, buildings[j])){
						int[] south = {2, b.getBuildingNumber(), buildings[j].getBuildingNumber()};
						System.out.println("Building " + b.getBuildingNumber() + " is south of Building " + buildings[j].getBuildingNumber()); 
						descr.add(south); 
					}
					if(isEast(b, buildings[j])){
						int[] east = { 3, b.getBuildingNumber(), buildings[j].getBuildingNumber()};
						System.out.println("Building " + b.getBuildingNumber() + " is east of Building " + buildings[j].getBuildingNumber()); 
						descr.add(east); 
					}
					if(isWest(b, buildings[j])){
						int[] west = { 4, b.getBuildingNumber(), buildings[j].getBuildingNumber()};
						System.out.println("Building " + b.getBuildingNumber() + " is west of Building " + buildings[j].getBuildingNumber()); 
						descr.add(west); 
					}
			}
			System.out.println("des size " + descr.size()); 
			buildingDescriptions.put(b.getBuildingNumber(), descr); 
			System.out.println(""); 

		return buildingDescriptions;
	}
	
	public HashMap<Integer, ArrayList<int[]>> compareBuildings(HashMap<Integer, ArrayList<int[]>> descriptions, Building[] buildings){
		HashMap<Integer, ArrayList<int[]>> buildingDescriptions = descriptions; 
		for(int i = 1; i < buildings.length; i++){
			ArrayList<int[]> descr = new ArrayList<int[]>();
			for(int j = 1; j < buildings.length; j++){
				if(i!=j){
					if(isNear(buildings[i], buildings[j])){
						//System.out.println("Building " + buildings[i].getBuildingNumber() + " is near Building " + buildings[j].getBuildingNumber()); 
						int[] near = {0, buildings[i].getBuildingNumber(), buildings[j].getBuildingNumber()}; 
						descr.add(near); 
					}
					if(isNorth(buildings[i], buildings[j])){
						int[] north = {1, buildings[i].getBuildingNumber(), buildings[j].getBuildingNumber()}; 
						//System.out.println("Building " + buildings[i].getBuildingNumber() + " is north of Building " + buildings[j].getBuildingNumber()); 
						descr.add(north); 
					}
					if(isSouth(buildings[i], buildings[j])){
						int[] south = {2, buildings[i].getBuildingNumber(), buildings[j].getBuildingNumber()};
						descr.add(south); 
					}
					if(isEast(buildings[i], buildings[j])){
						int[] east = { 3, buildings[i].getBuildingNumber(), buildings[j].getBuildingNumber()};
						//System.out.println("Building " + buildings[i].getBuildingNumber() + " is east of Building " + buildings[j].getBuildingNumber()); 
						descr.add(east); 
					}
					if(isWest(buildings[i], buildings[j])){
						int[] west = { 4, buildings[i].getBuildingNumber(), buildings[j].getBuildingNumber()};
						descr.add(west); 
					}
				}//end outer if
			}
			//System.out.println("des size " + descr.size()); 
			buildingDescriptions.put(buildings[i].getBuildingNumber(), descr); 
			System.out.println(""); 
		}//end outer for
		return buildingDescriptions; 
	}
	
	public HashMap<Integer, ArrayList<int[]>> reduceDescriptions(HashMap<Integer, ArrayList<int[]>> descriptions, Building[] buildings, Building b1){
		HashMap<Integer, ArrayList<int[]>> buildingDescr = descriptions;
		//System.out.println("this size " + descriptions.size());
		for(int  i =0; i < descriptions.size(); i++){//each building
			//ArrayList<int[]> descrs = buildingDescr.get(i); //for all compare
			ArrayList<int[]> descrs = buildingDescr.get(b1.getBuildingNumber()); //for one compare
			//System.out.println("that size " + descrs.size()); 
			//ArrayList<int[]> tempDescrs = new ArrayList<int[]>();
			//tempDescrs = descrs; 
			ArrayList<int[]> tempDescrs = new ArrayList<int[]>(descrs);
			//System.out.println("size " + descrs.size()); 
			for(int a = 0; a < descrs.size(); a++){//each description
				//System.out.println("size a " + i); 
				for(int b= 0; b < descrs.size(); b++){
					int[] tempB = descrs.get(b); 
					//System.out.println("TempB " + b); 
					//System.out.println("sizeee: " + descrs.size()); 
					//System.out.println("TempA " + a); 
					int[] tempA = descrs.get(a);
					
					if((a!=b) &&(tempA[0]==1) &&(tempB[0]==1) &&(isNorth(buildings[tempA[2]], buildings[tempB[2]]))){
						tempDescrs.remove(tempB); 
						System.out.println("removed north " + tempB[2]); 
					}
					if((a!=b) &&(tempA[0]==2) &&(tempB[0]==2) &&(isSouth(buildings[tempA[2]], buildings[tempB[2]]))){
						tempDescrs.remove(tempB); 
						System.out.println("removed south " + tempB[2]); 
					}
					if((a!=b) &&(tempA[0]==3) &&(tempB[0]==3) &&(isEast(buildings[tempA[2]], buildings[tempB[2]]))){
						tempDescrs.remove(tempB); 
						System.out.println("removed esst " + tempB[2]); 
					}
					if((a!=b) &&(tempA[0]==4) &&(tempB[0]==4) &&(isWest(buildings[tempA[2]], buildings[tempB[2]]))){
						tempDescrs.remove(tempB); 
						System.out.println("removed west " + tempB[2]); 
					}
				}
			}
			buildingDescr.put(i, tempDescrs); 
		}
		
		return buildingDescr;
	}
	
	public boolean isNorth(Building s,Building t){
		boolean isNorth = false;
		if(s.getCenterOfMassY() < t.getCenterOfMassY()){
			isNorth = true; 
		}
		return isNorth; 
	}
	
	public boolean isSouth(Building s,Building t){
		boolean isSouth = false;
		if(s.getCenterOfMassY() > t.getCenterOfMassY()){
			isSouth = true; 
		}
		return isSouth; 
	}
	
	public boolean isEast(Building s, Building t){
		boolean isEast = false;
		if(s.getCenterOfMassX() > t.getCenterOfMassX()){
			isEast = true; 
		}
		return isEast; 
	}
	
	public boolean isWest(Building s, Building t){
		boolean isWest = false;
		if(s.getCenterOfMassX() < t.getCenterOfMassX()){
			isWest = true; 
		}
		return isWest; 
	}
	
	public boolean isNear(Building s, Building t){
		boolean isNear = false;
		int x = Math.abs(s.getCenterOfMassX()-t.getCenterOfMassX()); 
		int y = Math.abs(s.getCenterOfMassY()-t.getCenterOfMassY()); 
		//System.out.println(t.getBuildingNumber()); 
		//System.out.println("xy " + x + ","+ y); 
		if((x<75) && (y<100)){
			isNear = true; 
			//System.out.println("true: " + x + ","+ y); 
		}
		return isNear; 
	}
	
	public static void main( String[] args )
	{
		  System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		  WhereDescriptions whereDescriptions = new WhereDescriptions(); 
		  WhatDescriptions what = new WhatDescriptions(); 
		  what.run(); 
		  Building[] buildings = what.getBuildings(); 
		  whereDescriptions.run(buildings); 
	}
}
