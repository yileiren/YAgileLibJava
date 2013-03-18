package ylr.YJson;

public class JSONTest
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		JSONObject o = new JSONObject();
		o.put("dd", "dd");
		o.put("num", 12);
		System.out.println(o.isNull("ggggg"));
		
		
		JSONArray a = new JSONArray();
		a.put(o);
		a.put(o);
		a.put(o);
		
		JSONObject u = new JSONObject();
		u.put("array", a);
		
		System.out.println(a.toString());
		System.out.println(u.toString());
		
		JSONObject build = new JSONObject(u.toString());
		System.out.println(build.toString());
	}

}
