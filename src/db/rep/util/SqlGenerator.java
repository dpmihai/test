package db.rep.util;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import db.rep.bean.Field;
import db.rep.bean.Report;

/**
 * @author Decebal Suiu
 */
public class SqlGenerator {
    
    public static final String BLANK = " ";
    public static final String COMMA = ",";
    public static final String DOT = ".";
    public static final String SELECT = "SELECT";
    public static final String FROM = "FROM";
    public static final String WHERE = "WHERE";
    
    private Report report;
    
    public SqlGenerator(Report report) {
        this.report = report;
    }
    
    public String generateSql() {
        return generateSelect();
    }
    
    private String generateSelect() {
        List fields = report.getFields();
        int fieldsCounter = fields.size();
        
        if (fieldsCounter == 0) {
            return "";
        }
        
        StringBuffer sql = new StringBuffer(SELECT);
        sql.append(BLANK);
        
        Set selectedTables = new TreeSet();
        
        for (int i = 0; i < fieldsCounter; i++) {
            Field field = (Field) fields.get(i);
            sql.append(field.object);
            sql.append(DOT);
            sql.append(field.fieldOrExpression);
            sql.append(COMMA);
            
            // add table to selected tables
            selectedTables.add(field.object);
        }
        sql.deleteCharAt(sql.length() - 1);
        
        sql.append(BLANK);
        sql.append(FROM);
        sql.append(BLANK);

        Iterator it = selectedTables.iterator();
        while (it.hasNext()) {
            sql.append((String) it.next());
            sql.append(COMMA);
        }
        sql.deleteCharAt(sql.length() - 1);
        
        return sql.toString();
    }
    
    /*private String generateSelect() {
		StringBuffer s = new StringBuffer(SELECT);
		s.append(" ");
		Iterator i = r.getFields().iterator();
		boolean first=true, ok = true;
		ArrayList al = getNeededTables(r);
		while (i.hasNext())
		{
			Field f = (Field) i.next();	
			if (f.getVisible())
			{
				ok = false;
				if (!first)	s = s.append(",");
				first = false;
				String tablename = f.getTable()+"."+f.getName();
				if ((f.getTable()).equals("VIRTUAL"))
				{
					if (!f.getType().equals(ReportGenVo.DATE))
						tablename = expand(r, f.getExpression()) + " as " + f.getName();
					else	
						tablename = expand(r, f.getExpression());
				}
				if (f.getType().equals(ReportGenVo.DATE))
				{
					String gb = f.getGroupBy();
					if(gb.equals("")||gb.equals("FIELD"))
						s = s.append("TO_CHAR("+tablename+",'dd/mm/yyyy')");
					else
						if(gb.equals("HOUR"))
							s = s.append("TO_CHAR("+tablename+",'hh')");
					else
						if(gb.equals("DAY"))
							s = s.append("TO_CHAR("+tablename+",'dd')");
					else
						if(gb.equals("WEEK"))
							s = s.append("TO_CHAR("+tablename+",'ww')");
					else
						if(gb.equals("MONTH"))
							s = s.append("TO_CHAR("+tablename+",'mm')");
					else
						if(gb.equals("YEAR"))
							s = s.append("TO_CHAR("+tablename+",'yy')");
					else	
						s = s.append("TO_CHAR("+tablename+",'dd/mm/yyyy')");
					if ((f.getTable()).equals("VIRTUAL"))
						s = s.append(" as " + f.getName());
					else
						s = s.append(" as DATE_" + Math.abs((new String(f.getTable()+"_"+f.getName())).hashCode()));
				}	
				else
					s = s.append(tablename);
			}
		}
		if(ok) return "No query yet";
		s = s.append(" from ");
		first = true;
		i = al.iterator();
		while (i.hasNext())
		{
			String st = (String) i.next();
			if (!first)	s = s.append(",");
			first = false;
			s = s.append(st);
		}
		if (r.getJoins().size()>0)
		{
			s = s.append(" where ");
			first = true;
			i = r.getJoins().iterator();
			while (i.hasNext())
			{
				Pair st = (Pair) i.next();
				if (!first)	s = s.append(" AND ");
				first = false;
				s = s.append(st.getLeft().getTable()+"."+st.getLeft().getName()+"="+st.getRight().getTable()+"."+st.getRight().getName());
				if(st.getType()==2) s = s.append("(+)");
			}
		}
		return s.toString();
	}
	
	private String generateWhere(ReportGenVo r) {
		StringBuffer s=new StringBuffer("");
		Iterator i;
		boolean first;
		if (r.getWheres().size()>0)
		{
			if (r.getJoins().size()==0)	{
				first = true;
			} else 
				first = false;
			i = r.getWheres().iterator();
			Where w;
			String func;
			Field f;
			while (i.hasNext())
			{
				w = (Where) i.next();
				f = w.getField();
				if (!getMember(w).equals(""))
				{
					if (!first)	s = s.append(" AND ");
						else s = s.append(" where ");
					first = false;
					func = w.getFieldfunction();
					if(!func.equals("")) s = s.append(func+"(");
					if (f.getType().equals(ReportGenVo.DATE)) {
						s = s.append("trunc("+f.getTable()+"."+f.getName()+")");
					} else {
						s = s.append(f.getTable()+"."+f.getName());
					}
					if(!func.equals("")) s = s.append(")");
					s = s.append(" "+w.getOperator()+" ");
					if(!func.equals("")) s = s.append(func+"(");
					if (f.getType().equals(ReportGenVo.VARCHAR2))
					{
						s = s.append("'");
						if (w.getOperator().equals("LIKE"))
						{
							s = s.append("%"+getMember(w)+"%");
						}
						else
							s = s.append(getMember(w));
						s = s.append("'");
					}
					else
					if (f.getType().equals(ReportGenVo.DATE))
					{
						s = s.append("TO_DATE('");
						s = s.append(getMember(w));
						s = s.append("','dd/MM/yyyy')");
					}
					else 
						s = s.append(getMember(w));				
					if(!func.equals("")) s = s.append(")");
				}
			}
		}
		return s.toString();
	}
	
	private String generateOrder(ReportGenVo r) {
		StringBuffer s=new StringBuffer("");
		boolean first;
		Iterator i;
		if (r.getOrderBy().size()>0)
		{
			s = s.append(" order by ");
			first = true;
			i = r.getOrderBy().iterator();
			while (i.hasNext())
			{
				Field f = (Field) i.next();
				if (!first)	s = s.append(",");
				first = false;
				if(f.getTable().equals("VIRTUAL"))
					s = s.append(f.getName()+" "+f.getSort());
				else
	//					if (!f.getType().equals(ReportGenVo.DATE))
						s = s.append(f.getTable()+"."+f.getName()+" "+f.getSort());
	//					else
	//						s = s.append("DATE_" + Math.abs((new String(f.getTable()+"_"+f.getName())).hashCode())+" "+f.getSort());
			}
		}
		return s.toString();
	}
	
    public String generateSQL(ReportGenVo r, boolean ignore)
    {
		StringBuffer s=new StringBuffer("");
		if (r.getSQL()==null||ignore) {
			s.append(generateSelect(r));
	    	s.append(generateWhere(r));
			s.append(generateOrder(r));
		} else {
			String sql = r.getSQL();
			String w = generateWhere(r);
			int iw;
			if ((iw = w.indexOf(" where "))>=0) {
				w = w.substring(iw+7);
			}
			s.append(Operations.insertWhere(sql, w));
		}
		System.err.println("Report sql:"+s);
    	return s.toString();
    }*/    

}
