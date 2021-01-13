package CacheProject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.*;

public class test1 {
	
	HashMap<Integer,Long> AssociateCacheL1=new HashMap<Integer,Long>();// Cache  used  for  Associative Mapping .
	HashMap<Integer,Long> AssociateCacheL2=new HashMap<Integer,Long>();
	HashMap<Integer,Integer> CurrentCache=new HashMap<Integer,Integer>();
	ArrayList<ArrayList<Long>> MainMemory;
	ArrayList<ArrayList<Long>> CacheMemoryL1;
	ArrayList<ArrayList<Long>> CacheMemoryL2;
	HashMap<Integer,String> SetTag=new HashMap<Integer,String>();
	HashMap<Integer,Long> SetCacheL1=new HashMap<Integer,Long>();
	HashMap<Integer,Long> SetCacheL2=new HashMap<Integer,Long>();
	static int Lines;//No. of Block lines in Main Memory .
	static int blockSize;
	static int MainSize;
	static int CacheLines;
	static int currentCacheLevel=1;
	static int level1;
	static int level2;
	static String firstElement;
	Queue<String> temp = new LinkedList<>();
	test1(int Mode,int N,int CL,int bSize){
		
		if(Mode==1) {
			Lines=N/bSize;
			blockSize=bSize;
			MainSize=N;
			CacheLines=CL;
			MainMemory=new ArrayList<ArrayList<Long>>();
			CacheMemoryL1=new ArrayList<ArrayList<Long>>();
			CacheMemoryL2=new ArrayList<ArrayList<Long>>();
			for(int i=0;i<Lines;i++) {
				MainMemory.add(new ArrayList<Long>());
			}
			for(int j=0;j<CL;j++) {
				CacheMemoryL1.add(new ArrayList<Long>());
				CacheMemoryL2.add(new ArrayList<Long>());
			}
			initializeDirect(Lines,bSize,CL); 
		}
		else if(Mode==2) {
			Lines=N/bSize;
			blockSize=bSize;
			MainSize=N;
			CacheLines=CL;
			MainMemory=new ArrayList<ArrayList<Long>>();
			for(int i=0;i<Lines;i++) {
				MainMemory.add(new ArrayList<Long>());
			}
			initializeAssociate(Lines,bSize,CL);
		}
		else if(Mode==3) {
			Lines=N/bSize;
			blockSize=bSize;
			MainSize=N;
			CacheLines=CL;
			MainMemory=new ArrayList<ArrayList<Long>>();
			for(int i=0;i<Lines;i++) {
				MainMemory.add(new ArrayList<Long>());
			}
			initializeSetAssociate(Lines,bSize,CL);
		}
	}
	
	private void initializeDirect(int Lines,int bSize,int CL) {
		
		for(int i=0;i<Lines;i++) {
			for(int j=0;j<bSize;j++) {
				MainMemory.get(i).add((long)0);
			}
		}
		for(int j=0;j<Lines;j++) {
			int x=j%CL;
			CacheMemoryL2.get(x).add((long)0);
			CacheMemoryL1.get(x).add((long)0);
		}
	}
	
	public void initializeAssociate(int Lines,int bSize,int CL) {
		
		for(int i=0;i<Lines;i++) {
			for(int j=0;j<bSize;j++) {
				MainMemory.get(i).add((long)0);
			}
		}
	}
	
	public void initializeSetAssociate(int Lines,int bSize,int CL) {
		
		for(int i=0;i<Lines;i++) {
			for(int j=0;j<bSize;j++) {
				MainMemory.get(i).add((long)0);
			}
		}
	}
	
	public static int log2(int N) {        
        
		int result = (int)(Math.log(N) / Math.log(2)); 
        return result; 
    }
	
	public void performOperationDirect() throws IOException{
		
		System.out.println(" Select  the  Operation  :- \n\n 1.for Loading in Main Memory  \n\n 2.for Loading from Main to Cache  \n\n 3.for Searching in Cache");
		System.out.println();
		System.out.print(" Operation  Selected  : ");
		int op=Reader.nextInt();
		System.out.println();
		int paBits=log2(MainSize);// physical address bits .
		int blockBits=log2(blockSize);// block address bits .
		int CacheLineBits=log2(CacheLines);// CacheLine bits .
		int TagBits=paBits-blockBits-CacheLineBits;
		int sizeLevel2=CacheLines*blockSize;
		int sizeLevel1=sizeLevel2/2;
		
		if(op==1){
			// Storing Data in Main Memory .
			System.out.print(" Enter  Data  To  be  Stored  : ");
			long data=Reader.nextLong();
			System.out.println();
			System.out.print(" Enter  Address  At  which  you  want  to  Store  in  Main  Memory (in "+paBits+" bits) : ");
			String s=Reader.next();
			System.out.println();
			int g=paBits-blockBits;
			String a=s.substring(0,g);
			int blockNumber=Integer.parseInt(a,2);
			String b=s.substring(g);
			int blockOffset=Integer.parseInt(b, 2);
			MainMemory.get(blockNumber).set(blockOffset,data);
			System.out.println(" Successfully  Stored  at  Block Number (binary) : "+a+"  and  Word  Number (binary): "+b+"  of  Main  Memory");
			System.out.println();
		}
		else if(op==2){
			// Loading Data from Main Memory to Cache Memory .
			System.out.print(" Enter  Address  of  the  data  currently  Stored  in  Main  Memory  : ");
			String s=Reader.next();
			System.out.println();
			int g=paBits-blockBits;
			String a=s.substring(0,g);
			int blockNumber=Integer.parseInt(a,2);
			String b=s.substring(g);
			int blockOffset=Integer.parseInt(b, 2);
			long data=MainMemory.get(blockNumber).get(blockOffset);// Fetched  Data  From  Main Memory .
			String q=s.substring(TagBits,paBits-blockBits);
			int CacheLine=Integer.parseInt(q, 2);
			String y=s.substring(TagBits+CacheLineBits);
			int blockOff=Integer.parseInt(y, 2);
			String m=s.substring(0,TagBits);
			int TagNumber=Integer.parseInt(m, 2);
			CurrentCache.put(CacheLine, TagNumber);
			if(level1<sizeLevel1) {
				CacheMemoryL1.get(CacheLine).set(blockOff, data);		
				level1+=1;
				System.out.println(" Successfully  Stored  at  CacheLine (binary) : "+q+" and  Word  Number (binary): "+y+"  with  TagNumber  (binary) : "+m+"  of  Level-1  Cache  Memory ");
			}
			else if(level2<sizeLevel2) {
				level2+=1;
				CacheMemoryL2.get(CacheLine).set(blockOff, data);		
				System.out.println(" Successfully  Stored  at  CacheLine (binary) : "+q+" and  Word  Number (binary): "+y+"  with  TagNumber  (binary) : "+m+"  of  Level-2  Cache  Memory ");
			}
			else {
				System.out.println(" Both  Cache  are  currently  filled ,  Do  you  Want  Replacement  and  load  this  data  in  Cache (y/n) ? ");
				String d=Reader.next();
				d=d.toLowerCase();
				if(d.contentEquals("y")) {
					if(CacheMemoryL1.get(CacheLine).size()!=0) {
						long dat=CacheMemoryL1.get(CacheLine).remove(0);
						CacheMemoryL1.get(CacheLine).set(blockOff, data);
						CurrentCache.put(CacheLine, TagNumber);
						System.out.println(" Removed  Data : "+dat+"  from  CacheLine  : "+q+"  at  Level-1  Cache ");
						System.out.println();
						System.out.println(" Currently  Loaded  Data : "+data+" at  CacheLine  : "+q+"  and  Word  Number  : "+y);
					}
					else {
						long dat=CacheMemoryL2.get(CacheLine).remove(0);
						CacheMemoryL2.get(CacheLine).set(blockOff, data);
						CurrentCache.put(CacheLine, TagNumber);
						System.out.println(" Removed  Data : "+dat+"  from  CacheLine  : "+q+"  at  Level-2  Cache ");
						System.out.println();
						System.out.println(" Currently  Loaded  Data : "+data+" at  CacheLine  : "+q+"  and  Word  Number  : "+y);
					}
				}
			}
		}
		else if(op==3){
			// Searching  Data  in  Cache  Memory  .
			System.out.print(" Enter  Address  of  the  data  to  be  searched  in  Cache  Memory : ");
			String s=Reader.next();
			System.out.println();
			String h=s.substring(TagBits,paBits-blockBits);
			int CacheLine=Integer.parseInt(h,2);
			String k=s.substring(0,TagBits);
			int TagNumber=Integer.parseInt(k, 2);
			String y=s.substring(TagBits+CacheLineBits);
			int blockOff=Integer.parseInt(y, 2);
			
			if(CurrentCache.get(CacheLine)==null) {
				System.out.println(" This Cache line is currently Empty");
				System.out.println();
				System.out.print(" Want to  Add  data  to  this  Cache  line (y/n) ?  : ");
				String a=Reader.next();
				System.out.println();
				a.toLowerCase();
				if(a.contentEquals("y")) {
					System.out.print(" Enter  the  Data :");
					long data=Reader.nextLong();
					int g=paBits-blockBits;
					String ad=s.substring(0,g);
					int blockNumber=Integer.parseInt(ad,2);
					String bc=s.substring(g);
					int blockOffset=Integer.parseInt(bc, 2);
					MainMemory.get(blockNumber).set(blockOffset,data);
					CurrentCache.put(CacheLine, TagNumber);
					System.out.println(" Successfully  Stored  at  block  Number  (binary): "+ad+"  and  Word  Number  (binary): "+bc+"  of  Main  Memory");
					CurrentCache.put(CacheLine, TagNumber);
					System.out.println();
					if(currentCacheLevel==1) {
						CacheMemoryL1.get(CacheLine).set(blockOff, data);		
						currentCacheLevel=2;
						System.out.println(" Successfully  Stored  at  CacheLine (binary) : "+h+" and  Word  Number (binary): "+y+"  with  TagNumber  (binary) : "+k+"  of  Level-1  Cache  Memory ");
					}
					else if(currentCacheLevel==2) {
						currentCacheLevel=1;
						CacheMemoryL2.get(CacheLine).set(blockOff, data);		
						System.out.println(" Successfully  Stored  at  CacheLine (binary) : "+h+" and  Word  Number (binary): "+y+"  with  TagNumber  (binary) : "+k+"  of  Level-2  Cache  Memory ");
					}
					System.out.println();
				}
			}
			else if(CurrentCache.get(CacheLine)==TagNumber) {
				
				if(CacheMemoryL1.get(CacheLine).get(blockOff)==0) {
					System.out.println(" Cache  Miss  Occured  at  Level-1 ");
					System.out.println();
					System.out.println(" Cache  Hit  Occured  at  Level-2 ");
					System.out.println();
					System.out.println(" Data  Found  : "+CacheMemoryL2.get(CacheLine).get(blockOff)+" at  Cacheline  (binary): "+h+" and  Word  Number (binary): "+y+"  with  TagNumber  (binary): "+k+"  of  Cache  Memory");
				}
				else if(CacheMemoryL2.get(CacheLine).get(blockOff)==0) {
					System.out.println(" Cache  Hit  Occured  at  Level-1  Cache ");
					System.out.println();
					System.out.println(" Data  Found  : "+CacheMemoryL1.get(CacheLine).get(blockOff)+" at  Cacheline  (binary): "+h+" and  Word  Number (binary): "+y+"  with  TagNumber  (binary): "+k+"  of  Cache  Memory");
				}
				System.out.println();
			}
			else {
				System.out.println(" Cache  miss  occured  at  both  level-1  and  level-2  Cache");
				System.out.print(" Want  to  replace  this  block  with  current  cacheline  block (y/n)  ?  : ");
				String d=Reader.next();
				System.out.println();
				d.toLowerCase();
				if(d.contentEquals("y")) {
					int g=paBits-blockBits;
					String ad=s.substring(0,g);
					int blockNumber=Integer.parseInt(ad,2);
					long datas=MainMemory.get(blockNumber).get(blockOff);
					String q=s.substring(TagBits,paBits-blockBits);
					int CacheLines=Integer.parseInt(q, 2);
					String yq=s.substring(TagBits+CacheLineBits);
					int blockOf=Integer.parseInt(yq, 2);
					String m=s.substring(0,TagBits);
					int TagNumbers=Integer.parseInt(m, 2);
					CurrentCache.put(CacheLines, TagNumbers);
					if(currentCacheLevel==1) {
						CacheMemoryL1.get(CacheLines).set(blockOf, datas);		
						currentCacheLevel=2;
						System.out.println(" Successfully  Replaced , Now  desired  Data  is  present   at  CacheLine  (binary): "+q+" and  Word  Number (binary): "+yq+"  with  TagNumber  (binary): "
								+m+"  of  Cache  Memory ");
					}
					else if(currentCacheLevel==2) {
						currentCacheLevel=1;
						CacheMemoryL2.get(CacheLines).set(blockOf, datas);		
						System.out.println(" Successfully  Replaced , Now  desired  Data  is  present   at  CacheLine  (binary): "+q+" and  Word  Number (binary): "+yq+"  with  TagNumber  (binary): "
								+m+"  of  Cache  Memory ");
					}
					System.out.println();
				}
			}
		}
	}
	
	public void performOperationAssociate() throws IOException {
		
		System.out.println(" Select  the  Operation  :- \n\n 1.for Loading in Main Memory  \n\n 2.for Loading from Main to Cache  \n\n 3.for Searching in Cache");
		System.out.println();
		System.out.print(" Operation  Selected  : ");
		int op=Reader.nextInt();
		System.out.println();
		int paBits=log2(MainSize);// physical address bits .
		int blockBits=log2(blockSize);// block address bits .
		int TagBits=paBits-blockBits;
		int sizeLevel2=CacheLines*blockSize;
		int sizeLevel1=sizeLevel2/2;
		
		if(op==1){
			// Storing Data in Main Memory .
			System.out.print(" Enter  Data  To  be  Stored  : ");
			long data=Reader.nextLong();
			System.out.println();
			System.out.print(" Enter  Address  At  which  you  want  to  Store  in  Main  Memory (in "+paBits+" bits) : ");
			String s=Reader.next();
			System.out.println();
			String a=s.substring(0,TagBits);
			int blockNumber=Integer.parseInt(a,2);
			String b=s.substring(TagBits);
			int blockOffset=Integer.parseInt(b, 2);
			MainMemory.get(blockNumber).set(blockOffset,data);
			System.out.println(" Successfully  Stored  at  Block Number (binary): "+a+"  and  Word  Number  (binary): "+b+"  of  the  Main  Memory ");
			System.out.println();
			temp.add(a);
		}
		else if(op==2) {
			// Loading Data from Main Memory to Cache Memory .
			System.out.print(" Enter  Address  of  the  data  Stored  in  Main  Memory  :");
			String s=Reader.next();
			System.out.println();
			String a=s.substring(0,TagBits);
			int tagNumber=Integer.parseInt(a,2);
			String b=s.substring(TagBits);
			int blockOffset=Integer.parseInt(b, 2);
			long data=MainMemory.get(tagNumber).get(blockOffset);// Fetched  Data  From  Main Memory .
			int t=0;
			if(level1<sizeLevel1) {
				AssociateCacheL1.put(tagNumber, data);
				level1+=1;
				System.out.println(" Successfully  Stored  this  data  at  Tag  Number  (binary): "+a+"  and  Word  Number  (binary): "+b+"  of  the  Level-1  Cache  Memory ");
			}
			else if(level2<sizeLevel2) {
				AssociateCacheL2.put(tagNumber, data);
				level2+=1;
				System.out.println(" Successfully  Stored  this  data  at  Tag  Number  (binary): "+a+"  and  Word  Number  (binary): "+b+"  of  the  Level-2  Cache  Memory ");
			}
			else {
				System.out.println(" Both  Level-1  and  Level-2  Cache  are  filled ,  Do  you  want  replacement  and  add  this  data  to  Cache (y/n) ?");
				String d=Reader.next();
				d=d.toLowerCase();
				if(d.contentEquals("y")) {
					String h=temp.poll();
					int td=Integer.parseInt(h);
					AssociateCacheL1.remove(td);
					AssociateCacheL1.put(tagNumber, data);
					System.out.println(" Successfully  Replaced  the  block  present  at  TagNumber  : "+h+"  with  the  block  at TagNumber  : "+a);
				}
			}
			System.out.println();
		}
		else if(op==3) {
			// Searching Data from Cache Memory .
			System.out.print(" Enter  Address  of  the  data  Stored  in  Cache  Memory : ");
			String s=Reader.next();
			System.out.println();
			String a=s.substring(0,TagBits);
			int tagNumber=Integer.parseInt(a,2);
			String be=s.substring(TagBits);
			if(AssociateCacheL1.containsKey(tagNumber)){
				System.out.println(" Cache  Hit  Occured  at  Level-1 ");
				System.out.println();
				System.out.println(" Data  Found  : "+AssociateCacheL1.get(tagNumber)+"  at  Tag  Number  (binary) : "+a+" and  Word  Number  (binary) : "+be+"  of  the  Level-1  Cache  Memory ");
				System.out.println();
			}
			else if(AssociateCacheL2.containsKey(tagNumber)){
				System.out.println(" Cache  Miss  Occured  at  Level-1 ");
				System.out.println();
				System.out.println(" Cache  Hit  Occured  at  Level-2 ");
				System.out.println();
				System.out.println(" Data  Found  : "+AssociateCacheL2.get(tagNumber)+"  at  Tag  Number  (binary) : "+a+" and  Word  Number  (binary) : "+be+"  of  the  Level-1  Cache  Memory ");
				System.out.println();
			}
			else {
				String b=s.substring(TagBits);
				int blockOffset=Integer.parseInt(b, 2);
				long data=MainMemory.get(tagNumber).get(blockOffset);
				System.out.println(" Cache  Miss  Occured  at  both  Level-1  and  Level-2  of  Cache  Memory ");
				System.out.println();
				String h=temp.poll();
				int td=Integer.parseInt(h);
				AssociateCacheL1.remove(td);
				AssociateCacheL1.put(tagNumber, data);
				System.out.println(" Successfully  Replaced  the  block  present  at  TagNumber  : "+h+"  with  the  block  at TagNumber  : "+a+"  of  the  level-1  Cache ");
				System.out.println();
			}
		}
	}
	
	public void performOperationSetAssociate(int keys) throws IOException{
		
		int key=keys;
		System.out.println(" Select  the  Operation  :- \n\n 1.for Loading in Main Memory  \n\n 2.for Loading from Main to Cache  \n\n 3.for Searching in Cache");
		System.out.println();
		System.out.print(" Operation  Selected  : ");
		int op=Reader.nextInt();
		System.out.println();
		int paBits=log2(MainSize);// physical address bits .
		int blockBits=log2(blockSize);
		int NumSets=CacheLines/key;
		int ar[]=new int[NumSets+1];
		int setBits=log2(NumSets);
		int TagBits=paBits-setBits-blockBits;
		int sizeLevel2=CacheLines*blockSize;
		int sizeLevel1=sizeLevel2/2;
		
		
		if(op==1) {
			// Storing Data in Main Memory .
			int TagBit=paBits-blockBits;
			System.out.print(" Enter  Data  To  be  Stored  : ");
			long data=Reader.nextLong();
			System.out.println();
			System.out.print(" Enter  Address  At  which  you  want  to  Store  in  Main  Memory (in "+paBits+" bits) : ");
			String s=Reader.next();
			System.out.println();
			String a=s.substring(0,TagBit);
			int blockNumber=Integer.parseInt(a,2);
			String b=s.substring(TagBit);
			int blockOffset=Integer.parseInt(b, 2);
			MainMemory.get(blockNumber).set(blockOffset,data);
			System.out.println(" Successfully  Stored  at  Block  Number (binary): "+a+"  and  Word  Number  (binary): "+b+"  of  the  Main  Memory ");
			System.out.println();
		}
		else if(op==2) {
			System.out.print(" Enter  the  Address  of  the  data  to  store  in  Set  Cache  memory  : ");
			String s=Reader.next();
			System.out.println();
			int TagBit=paBits-blockBits;
			String a=s.substring(0,TagBit);
			int blockNumber=Integer.parseInt(a,2);
			String b=s.substring(TagBit);
			int blockOffset=Integer.parseInt(b,2);
			long data=MainMemory.get(blockNumber).get(blockOffset);
			String SetAddress=s.substring(TagBits,paBits-blockBits);
			String TagAddress=s.substring(0,TagBits);
			int k=Integer.parseInt(SetAddress);
			if(level1<sizeLevel1) {
				SetCacheL1.put(k, data);
				SetTag.put(k, TagAddress);
				level1+=1;
				System.out.println(" Successfully  Stored  this  data  in  the  Set (binary) : "+SetAddress+"  and  TagAddress  (binary):"+TagAddress+"  of  Level-1  Cache  Memory  ");
			}
			else if(level2<sizeLevel2) {
				SetCacheL2.put(k, data);
				SetTag.put(k, TagAddress);
				level2+=1;
				System.out.println(" Successfully  Stored  this  data  in  the  Set (binary) : "+SetAddress+"  and  TagAddress  (binary):"+TagAddress+"  of  Level-2  Cache  Memory  ");
			}
			else {
				SetCacheL1.remove(k);
				SetTag.remove(k);
				System.out.println(" Both  Level-1  and  Level-2  Cache  memory  are  full,  Performing  Replacement ");
				System.out.println();
				SetCacheL1.put(k, data);
				SetTag.put(k, TagAddress);
				System.out.println(" Successfully  Replaced  the  Currently  Loaded  block  of  Set : "+k+"  with  the  Desired  block  Cache  Memory ");
			}
			System.out.println();
		}
		else  if(op==3) {
			System.out.print(" Enter  the  Address  of  the  data  to  searched  in  Set  Cache  memory  : ");
			String s=Reader.next();
			System.out.println();
			String SetAddress=s.substring(TagBits,paBits-blockBits);
			String TagAddress=s.substring(0,TagBits);
			int k=Integer.parseInt(SetAddress);
			
			if(SetTag.get(k)==null) {
				System.out.println(" The  TagAddress  of  this  Set  is  Currrently  Empty  ");
				System.out.println();
				System.out.println(" Do  you  want  to  add  data  to  this  Tag  Adress  (y/n)  ?");
				String d=Reader.next();
				d=d.toLowerCase();
				if(d.contentEquals("y")) {
					int TagBit=paBits-blockBits;
					System.out.print(" Enter  Data  : ");
					long data=Reader.nextLong();
					System.out.println();
					System.out.print(" Enter  Address  At  which  you  want  to  Store  in  Main  Memory :");
					String S=Reader.next();
					System.out.println();
					String a=S.substring(0,TagBit);
					int blockNumber=Integer.parseInt(a,2);
					String b=S.substring(TagBit);
					int blockOffset=Integer.parseInt(b, 2);
					MainMemory.get(blockNumber).set(blockOffset,data);
					String SetAdd=S.substring(TagBits,paBits-blockBits);
					String TagAdd=S.substring(0,TagBits);
					int kk=Integer.parseInt(SetAdd);
					SetTag.put(kk,TagAdd);
					if(currentCacheLevel==1) {
						SetCacheL1.put(kk,data);
						currentCacheLevel=2;
						System.out.println(" Successfully  Stored  this  data  in  the  Set (binary) : "+SetAddress+"  and  TagAddress  (binary): "+TagAddress+"  of  Level-1  Cache  Memory  ");
					}
					else if(currentCacheLevel==2) {
						currentCacheLevel=1;
						SetCacheL2.put(kk, data);
						System.out.println(" Successfully  Stored  this  data  in  the  Set (binary) : "+SetAddress+"  and  TagAddress  (binary): "+TagAddress+"  of  Level-2  Cache  Memory  ");
					}
					System.out.println();
				}
			}
			else if(SetTag.get(k).contentEquals(TagAddress)) {
				int TagBit=paBits-blockBits;
				String a=s.substring(0,TagBit);
				int blockNumber=Integer.parseInt(a,2);
				String b=s.substring(TagBit);
				int blockOffset=Integer.parseInt(b, 2);
				long data=MainMemory.get(blockNumber).get(blockOffset);
				if(SetCacheL1.get(k)==null) {
					System.out.println(" Cache  Miss  Occured  at  Level-1  Cache ");
					System.out.println();
					System.out.println(" Cache  Hit  Occured  at  Level-2  Cache ");
					System.out.println();
					System.out.println(" Successfully  found  the  Data  : "+SetCacheL2.get(k)+"  at  Set  (binary) : "+SetAddress+"  and  TagAddress  (binary) : "+TagAddress);
				}
				else if(SetCacheL2.get(k)==null) {
					System.out.println(" Cache  Hit  Occured  at  Level-1  Cache ");
					System.out.println();
					System.out.println(" Successfully  found  the  Data  : "+SetCacheL1.get(k)+"  at  Set  (binary) : "+SetAddress+"  and  TagAddress  (binary) : "+TagAddress);
				}
				
			}
			else {
				int TagBit=paBits-blockBits;
				String a=s.substring(0,TagBit);
				int blockNumber=Integer.parseInt(a,2);
				String b=s.substring(TagBit);
				int blockOffset=Integer.parseInt(b, 2);
				long data=MainMemory.get(blockNumber).get(blockOffset);
				System.out.println(" Cache  Miss  Occured  at  both  Level-1  and  Level-2  of  Cache  Memory ");
				System.out.println();
				
				SetCacheL1.remove(k);
				SetTag.remove(k);
				SetCacheL1.put(k, data);
				SetTag.put(k, TagAddress);
				System.out.println(" Successfully ,  Replaced  the  currently  loaded  block  of  Set  "+k+"  with  the  desired  block  ");
				

			}
		}
	}
	 
	public static void main(String[] args) throws IOException{
		
		Reader.init(System.in);
		System.out.print(" Choose  Mode :- \n\n 1. Direct  Mapping  \n\n 2. Full  Associative  Mapping  \n\n 3. Set  Associative  Mapping ");
		System.out.println();
		System.out.println();
		System.out.print(" Mapping  Mode : ");
		int Mode=Reader.nextInt();
		System.out.println();
		System.out.print(" Enter  Main  Memory  Size  (in Bytes/Words)  :");
		int N=Reader.nextInt();
		System.out.println();
		System.out.print(" Enter  Number  of  Cache  Lines :");
		int CL=Reader.nextInt();
		System.out.println();
		System.out.print(" Enter  Block  Size  (same  for  both  Cache/Main  Memory) :");
		int bSize=Reader.nextInt();
		System.out.println();
		test1 t=new test1(Mode,N,CL,bSize);
		System.out.print(" No.  of  Queries  : ");
		int q=Reader.nextInt();
		System.out.println();
		
		if(Mode==1) {
			for(int i=0;i<q;i++) {
				t.performOperationDirect();
				System.out.println(" #####################################################################################################################################################################################");
				System.out.println();
			}
		}
		else if(Mode==2) {
			for(int i=0;i<q;i++) {
				t.performOperationAssociate();
				System.out.println(" #####################################################################################################################################################################################");
				System.out.println();
			}
		}
		else if(Mode==3) {
			System.out.print(" Enter  key  for  Set  Associative  Mapping  : ");
			int key=Reader.nextInt();
			System.out.println();
			for(int i=0;i<q;i++) {
				t.performOperationSetAssociate(key);
				System.out.println(" #####################################################################################################################################################################################");
				System.out.println();
			}
		}
		else {
			System.out.println(" Invalid  Mode \""+Mode+"\",  System  Exit  Occured...");
		}
	}
}

class Reader {
    static BufferedReader reader;
    static StringTokenizer tokenizer;
   /** call this method to initialize reader for InputStream */
    static void init(InputStream input){
        reader = new BufferedReader(
         new InputStreamReader(input));
        tokenizer = new StringTokenizer("");
    }
   /** get next word */
    static String next() throws IOException{
        while ( ! tokenizer.hasMoreTokens() ){
            //TODO add check for eof if necessary
            tokenizer = new StringTokenizer(
                   reader.readLine() );
        }
        return tokenizer.nextToken();
    }
    static int nextInt() throws IOException {
        return Integer.parseInt( next() );
    }
    static double nextDouble() throws IOException {
        return Double.parseDouble( next() );
    }
    static long nextLong() throws IOException{
    	return Long.parseLong(next());
    }
}







