package Booths_algo;                               // Booth's_Algorithm_Implementattion

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.nio.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.lang.Math;
import java.io.*;

public class Booths_algorithm
{
	static List<String> Accm = new ArrayList<>();
	static List<String> Q = new ArrayList<>();
	static List<String> opt = new ArrayList<>();
	static List<Character> queue = new ArrayList<>();
	static List<Integer> count = new ArrayList<>();

	static String Mult=new String("");
	static String mul=new String("");
	static String Acc=new String("");

	static char q='0';
	static int ab=0;

	public static void main(String[] args)throws IOException
	{
		Long x=Long.MAX_VALUE;

		File fout = new File("result.txt");
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		Reader.init(System.in);

		System.out.print(" ENTER FIRST NUMBER IN DECIMAL :");
		long a=Reader.nextLong();
		System.out.println();

		System.out.print(" ENTER SECOND NUMBER IN DECIMAL :");
		long b=Reader.nextLong();


		Mult=func(a);//multiplicand
		mul=func(b);//multiplier

		if(Mult.length()>mul.length())
		{
			mul=Binary2(b,Mult.length());
		}
		else if(Mult.length()<mul.length())
		{
			Mult=Binary2(a,mul.length());
		}

		Acc=accumulator(Mult.length());

		int bits=Acc.length();

		Accm.add(Acc);
		Q.add(mul);
		opt.add("Initialised");
		count.add(bits);
		queue.add(q);
		String neg=comp_2(Mult);

		String asd=" NUMBER A (DECIMAL) : "+a;
		bw.write(asd);
		bw.newLine();
		bw.newLine();

		String asdd=" NUMBER B (DECIMAL) : "+b;
		bw.write(asdd);
		bw.newLine();
		bw.newLine();

		String as=" MULTIPLICAND (M) : "+Mult;
		bw.write(as);
		bw.newLine();
		bw.newLine();

		String da=" MULTIPLIER (Q) : "+mul;
		bw.write(da);
		bw.newLine();
		bw.newLine();

		String h=" (-M) : "+neg;
		bw.write(h);
		bw.newLine();
		bw.newLine();

		while(bits!=0)
		{
			char last=mul.charAt(mul.length()-1);

			if((last=='0' && q=='0') || (last=='1' && q=='1'))
			{
				char r=Acc.charAt(Acc.length()-1);
				char s=mul.charAt(mul.length()-1);

				Acc=Right_shift(Acc);
				mul=New_Right_shift(mul,r);
				q=s;
				bits--;

				Accm.add(Acc);
				Q.add(mul);
				opt.add("Arithmetic_right_shit");
				count.add(bits);
				queue.add(q);
			}

			else if(last=='1' && q=='0')
			{
				Acc=add_binary(Acc,neg);

				Accm.add(Acc);
				Q.add(mul);
				opt.add("Acc=Acc-M");
				count.add(bits);
				queue.add(q);

				char r=Acc.charAt(Acc.length()-1);
				char s=mul.charAt(mul.length()-1);

				Acc=Right_shift(Acc);
				mul=New_Right_shift(mul,r);
				q=s;
				bits--;

				Accm.add(Acc);
				Q.add(mul);
				opt.add("Arithmetic_right_shit");
				count.add(bits);
				queue.add(q);

			}

			else if(last=='0' && q=='1')
			{
				Acc=add_binary(Acc,Mult);
				char r=Acc.charAt(Acc.length()-1);
				char s=mul.charAt(mul.length()-1);

				Accm.add(Acc);
				Q.add(mul);
				opt.add("Acc=Acc+M");
				count.add(bits);
				queue.add(q);

				Acc=Right_shift(Acc);
				mul=New_Right_shift(mul,r);
				q=s;
				bits--;

				Accm.add(Acc);
				Q.add(mul);
				opt.add("Arithmetic_right_shit");//
				count.add(bits);
				queue.add(q);

			}
		}

		int size=mul.length()+6;

		String heading1="|"+padRight("OPERATION JUST PERFORMED",29)+"|"+padRight("  ACC",size)+"|"+padRight("   Q",size)+"|"+padRight("Q!",8)+"|"+padRight("COUNT",12)+"|";

		String d=design(heading1.length());

		bw.write(d);
		bw.newLine();

		bw.write(heading1);
		bw.newLine();

		bw.write(d);
		bw.newLine();
		bw.newLine();

		for(int j=0;j<Accm.size();j++)
		{
			String wr="|"+padRight(opt.get(j),29)+"|"+padRight(Accm.get(j),size)+"|"+padRight(""+Q.get(j)+"",size)+"|"+padRight(""+queue.get(j)+"",10)+"|"+padRight(""+count.get(j)+"",10)+"|";
			bw.write(wr);
			bw.newLine();
			bw.write(d);
			bw.newLine();
		}

		bw.newLine();
		Long z=a*b;
		String dff=" FINAL ANSWER IN DECIMAL IS : "+z;
		bw.write(dff);
		bw.newLine();

		bw.newLine();
		String df=" FINAL ANSWER IN BINARY IS : "+Acc+mul;
		bw.write(df);
		bw.close();

		System.out.println();

		System.out.println(" STEP BY STEP BOOTH\'S ALGORITHM : ");

		System.out.println();

		BufferedReader reader;

		try
		{
			reader = new BufferedReader(new FileReader("result.txt"));
			String line = reader.readLine();
			while (line != null)
			{
				System.out.println(line);
				line = reader.readLine();
			}
			reader.close();
		}

		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public static String design(int a)
	{
		String d=new String("");

		for(int i=0;i<a;i++)
		{
			d+="_";
		}

		return d;
	}

	public static String accumulator(int l)// Inintialize Accumulator
	{
		String x=new String("");
		for(int i=0;i<l;i++)
		{
			x+="0";
		}
		return x;
	}

	public static String func(long a)// to return binary number
	{
		if(a*1>0)
		{
			return "0"+Long.toBinaryString(a);
		}
		else if(a==0)
		{
			return "0000";
		}
		else
		{
			String h=Long.toBinaryString(a*-1);
			return "1"+comp_2(h);
		}
	}

	public static String padRight(String s, int n)// U
	{
	     return String.format("%-" + n + "s", s);
	}

	public static String Binary2(long x,int l)
	{
		if(x*1>0)
		{
			String y=Long.toBinaryString(x);
			int c=y.length();
			String z=new String("");
			for(int i=0;i<l-c;i++)
			{
				z+="0";
			}
			return z+y;
		}
		else if(x==0)
		{	String m=new String("");
			for(int k=0;k<l;k++)
			{
				m+="0";
			}
			return m;
		}
		else
		{
			String y=Long.toBinaryString(x*-1);
			int c=y.length();
			String z=new String("");
			for(int i=0;i<l-c;i++)
			{
				z+="1";
			}
			return z+y;
		}
	}

	public static String add_binary(String s1,String s2)
	{
		char ar1[]=s1.toCharArray();
		char ar2[]=s2.toCharArray();
		int a=0;
		String result=new String("");

		for(int i=ar1.length-1;i>=0;i--)
		{
			if(i!=0)
			{
				if(ar1[i]=='0' && ar2[i]=='0' && a==0)
				{
					result+=0;
				}
				else if(ar1[i]=='0' && ar2[i]=='1' && a==0)
				{
					result+=1;
				}
				else if(ar1[i]=='1' && ar2[i]=='0' && a==0)
				{
					result+=1;
				}
				else if(ar1[i]=='1' && ar2[i]=='1' && a==0)
				{
					result+='0';
					a=1;
				}
				else if(ar1[i]=='1' && ar2[i]=='1' && a==1)
				{
					result+=1;
					a=1;
				}
				else if(ar1[i]=='0' && ar2[i]=='0' && a==1)
				{
					result+='1';
					a=0;
				}
				else if(ar1[i]=='0' && ar2[i]=='1' && a==1)
				{
					result+='0';
					a=1;
				}
				else if(ar1[i]=='1' && ar2[i]=='0' && a==1)
				{
					result+='0';
					a=1;
				}
			}
			else
			{
				if(ar1[i]=='0' && ar2[i]=='0' && a==0)
				{
					result+='0';
				}
				else if(ar1[i]=='0' && ar2[i]=='1' && a==0)
				{
					result+='1';
				}
				else if(ar1[i]=='1' && ar2[i]=='0' && a==0)
				{
					result+=1;
				}
				else if(ar1[i]=='1' && ar2[i]=='1' && a==0)
				{
					result+='0';
				}
				else if(ar1[i]=='1' && ar2[i]=='1' && a==1)
				{
					result+=1;
				}
				else if(ar1[i]=='0' && ar2[i]=='0' && a==1)
				{
					result+='1';
				}
				else if(ar1[i]=='0' && ar2[i]=='1' && a==1)
				{
					result+='0';
				}
				else if(ar1[i]=='1' && ar2[i]=='0' && a==1)
				{
					result+='0';
				}
			}
		}

		char rev[]=result.toCharArray();
		String f=new String("");
		for(int j=rev.length-1;j>=0;j--)
		{
			f+=rev[j];
		}
		return f;
	}

	public static String comp_1(String r)//(1's complement)
	{
		char [] ar=r.toCharArray();
		String result=new String("");

		for(int i=0;i<r.length();i++)
		{
			if(ar[i]=='0')
			{
				result+='1';
			}
			else if(ar[i]=='1')
			{
				result+='0';
			}
		}

		return result;
	}

	public static String comp_2(String s)//(2's complement)
	{
		String d=comp_1(s);
		char [] ar=d.toCharArray();
		int a=1;
		String result=new String("");

		for(int i=ar.length-1;i>=0;i--)
		{
			if(i!=0)
			{
				if(ar[i]=='0' && a==0)
				{
					result+='0';
				}
				else if(ar[i]=='0' && a==1)
				{
					result+='1';
					a=0;
				}
				else if(ar[i]=='1' && a==0)
				{
					result+='1';
				}
				else if(ar[i]=='1' && a==1)
				{
				result+='0';
				a=1;
				}
			}
			else
			{
				if(ar[i]=='0' && a==0)
				{
					result+='0';
				}
				else if(ar[i]=='0' && a==1)
				{
					result+='1';

				}
				else if(ar[i]=='1' && a==0)
				{
					result+='1';
				}
				else if(ar[i]=='1' && a==1)
				{
				result+='0';

				}
			}
		}

		char rev[]=result.toCharArray();
		String f=new String("");
		for(int j=rev.length-1;j>=0;j--)
		{
			f+=rev[j];
		}
		return f;
	}

	public static String Right_shift(String s)
	{
		char c=s.charAt(0);
		s=c+s.substring(0,s.length()-1);
		return s;
	}

	public static String New_Right_shift(String s,char c)
	{
		s=c+s.substring(0,s.length()-1);
		return s;
	}

}
class Reader // CLASS FOR READING INPUTS
{
    static BufferedReader reader;
    static StringTokenizer tokenizer;

    /** call this method to initialize reader for InputStream */
    static void init(InputStream input)
    {
        reader = new BufferedReader(
         new InputStreamReader(input));
        tokenizer = new StringTokenizer("");
    }

    /** get next word */
    static String next() throws IOException {
        while ( ! tokenizer.hasMoreTokens() ) {
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
    static long nextLong() throws IOException {
        return Long.parseLong( next() );
    }
}
