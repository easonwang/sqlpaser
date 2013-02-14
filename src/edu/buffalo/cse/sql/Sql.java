/* Generated By:JavaCC: Do not edit this line. Sql.java */
package edu.buffalo.cse.sql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.buffalo.cse.sql.Schema.Column;
import edu.buffalo.cse.sql.data.Datum;
import edu.buffalo.cse.sql.data.Datum.CastError;
import edu.buffalo.cse.sql.data.Datum.Flt;
import edu.buffalo.cse.sql.plan.*;
import edu.buffalo.cse.sql.plan.AggregateNode.AggColumn;
import edu.buffalo.cse.sql.plan.ExprTree.OpCode;
import edu.buffalo.cse.sql.plan.PlanNode.*;

public class Sql {

	public static void main(String[] args) {
		System.out.println("You haven't implemented me yet!");
	}

	public static List<Datum[]> execQuery(
			Map<String, Schema.TableFromFile> tables, PlanNode q)
			throws SqlException {
		System.out.println("Something");
		System.out.println("Processing the Q: " + q.toString());
		boolean a = tables.containsKey("R");
		System.out.println("R is the key present " + a);
		// for (Map.Entry<String, Schema.TableFromFile> iterator : tables
		// .entrySet()) {
		// File file = iterator.getValue().getFile(); // read file for data
		// System.out.println("File name is " + file.toString());
		//
		// TupleIterator tblItr = new TupleIterator(iterator.getValue());
		//
		// Datum[] tuple;
		// while (tblItr.hasNext()) {
		// tuple = tblItr.readNext();
		// System.out.println(tuple);
		//
		// }
		// }
		QueryRead queRead = new QueryRead(tables);
		List<Datum[]> res = queRead.QueryEval(q);
		// Schema.Column tableColumns =

		// throw new SqlException("execQuery() is unimplemented");
		return res;
	}

	public static List<List<Datum[]>> execFile(File program)
			throws SqlException {
		throw new SqlException("execQuery() is unimplemented");
	}
}

class TupleIterator {
	File file;
	Schema.TableFromFile schema;
	int index;
	String[] data;
	int readidx;
	Datum[] array;
	BufferedReader CSVFile;

	public TupleIterator(Schema.TableFromFile schema) {
		file = schema.getFile();
		this.schema = schema;

		String filePath = file.toString();
		try {
			CSVFile = new BufferedReader(new FileReader(filePath));

		} catch (IOException e) {

		}
	}

	public Boolean hasNext() {
		if (CSVFile.markSupported()) {
			System.out.println("Mark is supported");
			try {
				CSVFile.mark((int) file.length());
				if (CSVFile.readLine() != null) {
					CSVFile.reset();
					return true;
				}
			} catch (IOException e) {

			}
		}
		return false;
	}

	public Datum[] readNext() {

		String dataRow = null;
		try {
			if (CSVFile != null) {
				dataRow = CSVFile.readLine();
				if (dataRow == null) {
					CSVFile.close();
					return null;
				}
			}
		} catch (IOException e) {

		}
		// split the file
		// File fd = new File(file);
		String[] dataArr = dataRow.split(","); // also remove the white spaces
		Datum[] tuple = new Datum[dataArr.length]; // for reading tuple
		int idx = 0;
		for (Schema.Column c : schema) {
			Datum data;
			switch (c.type) {
			case INT:
				// get int from ParseInt.String[index]
				data = new Datum.Int(Integer.parseInt(dataArr[idx]));
				tuple[idx] = data;
				idx++;
				break;
			case BOOL:
				data = new Datum.Bool(Boolean.parseBoolean(dataArr[idx]));
				tuple[idx] = data;
				idx++;
				break;
			case FLOAT:
				data = new Datum.Flt(Float.parseFloat(dataArr[idx]));
				tuple[idx] = data;
				idx++;
				break;
			case DATE:
				data = new Datum.Str(dataArr[idx]);
				tuple[idx] = data;
				idx++;
				break;
			case STRING:
				data = new Datum.Str(dataArr[idx]);
				tuple[idx] = data;
				idx++;
				break;
			default:
				break;
			}
		}
		if (idx == dataArr.length) {
			System.out.println("Read tuple is correct");
		}
		return tuple;
	}

	public void closeFile() {
		if (CSVFile != null) {
			try {
				CSVFile.close();
			} catch (IOException e) {

			}
		}

	}

}

class QueryRead {
	Map<String, Schema.TableFromFile> db;
	List<Schema.Var> univSchema = new ArrayList<Schema.Var>();

	public QueryRead(Map<String, Schema.TableFromFile> tables) {
		db = tables;
		Project.setAttrIdx(tables);
	}

	public List<Datum[]> QueryEval(PlanNode q) {
		List<Datum[]> res = null, left = null, right = null;

		switch (q.struct) {
		case BINARY:
			Binary bchild = (Binary) q;
			left = this.QueryEval(bchild.getLHS());
			right = this.QueryEval(bchild.getRHS());
			break;
		case UNARY:
			Unary uchild = (Unary) q;
			res = this.QueryEval(uchild.getChild());
			break;
		case LEAF:
			break;
		default:
			System.out.println("Wrong Structure");
			break;
		}
		switch (q.type) {
		case PROJECT:
			Project prj = new Project(db, q);
			res = prj.getNext(res);
			System.out.println("Project Query");
			break;
		case SELECT:
			Select sel = new Select(q);
			res = sel.getNext(res);
			System.out.println("Select Query");
			break;
		case SCAN:
			Scan sc = new Scan();
			res = sc.scanTable(db, (ScanNode) q);
			System.out.println("Scan Query");
			break;
		case JOIN:
			Join j = new Join();
			res = j.joinTables(left, right, (JoinNode) q, univSchema);
			System.out.println("Join Query");
			break;
		case NULLSOURCE:
			System.out.println("Nullsource Query");
			break;
		case UNION:
			System.out.println("Union Query");
			break;
		case AGGREGATE:
			Aggregate ag = new Aggregate();
			res = ag.getAggregate(res, (AggregateNode) q, db);
			System.out.println("Aggregate Query");
			break;
		default:
			System.out.println("Error in type");
			break;
		}
		return res;
	}
}

class Project {
	public static Map<String, Integer> projectionMap = new HashMap<String, Integer>();
	Map<String, Schema.TableFromFile> tables;
	PlanNode q = null;

	public Project(Map<String, Schema.TableFromFile> tables, PlanNode q) {
		this.tables = tables;
		this.q = q;
	}

	public static void setAttrIdx(Map<String, Schema.TableFromFile> tables) { // Call
																				// in
																				// the
																				// start
																				// of
																				// execQuery
																				// or
																				// in
																				// the
																				// start
																				// of
																				// every
																				// Projection
		projectionMap.clear();
		int position = 0;
		for (Map.Entry<String, Schema.TableFromFile> iterator : tables
				.entrySet()) {
			for (Schema.Column c1 : iterator.getValue()) {
				projectionMap.put(c1.getName(), position);
				position++;
			}
		}
	}

	public void setAttrIdx(Map<String, Schema.TableFromFile> tables, PlanNode q) { // Call
																					// When
																					// returning
																					// from
																					// getNext()
		projectionMap.clear();
		ProjectionNode pNode = (ProjectionNode) q;
		int position = 0;
		for (Map.Entry<String, Schema.TableFromFile> iterator : tables
				.entrySet()) {
			for (Schema.Column c1 : iterator.getValue()) {
				for (ProjectionNode.Column c : pNode.getColumns()) {
					if (c.name == c1.getName()) {
						projectionMap.put(c.name, position); // positions start
																// from zero
						position++;
						break;
					}
				}
			}
		}
	}

	public static int getAttrIdx(String attr) {
		for (String key : projectionMap.keySet()) {
			if (key.equals(attr)) {
				return projectionMap.get(key);
			}

		}
		return 0;
	}

	public List<Datum[]> getNext(List<Datum[]> db) {

		ArrayList<Datum[]> pResult = new ArrayList<Datum[]>();
		ProjectionNode pNode = (ProjectionNode) q;
		int size = q.getSchemaVars().size(); // size of each tuple
		for (Datum[] loop1 : db) {
			int i = 0;
			Datum[] tup = new Datum[size];
			for (ProjectionNode.Column c : pNode.getColumns()) {
				int attrIdx = getAttrIdx(c.expr.toString());
				tup[i++] = loop1[attrIdx];
			}
			pResult.add(tup);
		}

		setAttrIdx(tables, q); // sets according to present q
		if (!pResult.isEmpty())
			return pResult;
		else
			return null;
	}
}

class Scan {
	public List<Datum[]> scanTable(Map<String, Schema.TableFromFile> tables,
			ScanNode q) {
		List<Datum[]> db = new ArrayList<Datum[]>();
		for (Map.Entry<String, Schema.TableFromFile> iterator : tables
				.entrySet()) {
			if (q.table == iterator.getKey()) {
				File file = iterator.getValue().getFile(); // read file for data
				System.out.println("File name is " + file.toString());

				TupleIterator tblItr = new TupleIterator(iterator.getValue());

				Datum[] tuple;
				while (tblItr.hasNext()) {
					tuple = tblItr.readNext();
					System.out.println(tuple);
					db.add(tuple);
				}
				break;
			}
		}
		return db;
	}
}

class Aggregate {
	public List<Datum[]> getAggregate(List<Datum[]> inp, AggregateNode aNode,
			Map<String, Schema.TableFromFile> db) {
		List<AggColumn> cols;

		List<Datum[]> res = new ArrayList<Datum[]>();
		cols = aNode.getAggregates();
		Datum[] tres = new Datum[cols.size()];
		int index = 0;
		
		// facing issues here
		for (int i = 0; i < cols.size(); i++) {
			Map<String, String> aggrMap = new HashMap<String, String>();
			ExprTree exp = cols.get(i).expr;
			int match = 0;
			Column c = new Column(null, null, null);
			for (ExprTree itr : exp) {
				match = 0;
				for (Map.Entry<String, Schema.TableFromFile> iterator : db
						.entrySet()) {
					Schema.TableFromFile value = iterator.getValue();
					for (index = 0; index < value.size(); index++) {
						String vname1 = itr.toString();
						String vname2 = value.get(index).name.name;
						if (vname1.equals(vname2)) {
							c = value.get(index);
							aggrMap.put(vname1, c.getName());
							match = 1;
							break;
						}
					}
					if(match == 1)
						break;
				}
			}
			float ans = 0;

			switch (cols.get(i).aggType) {
			case SUM:
				for (Datum[] d : inp) {
					try {
						ans = ans + d[index].toFloat();
					} catch (CastError e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Datum sum = null;
				switch (c.type) {
				case INT:
					sum = new Datum.Int((int) ans);
					break;
				case FLOAT:
					sum = new Datum.Flt(ans);
					break;
				}
				tres[i] = sum;
				break;
			case COUNT:
				int cn = 0;
				for (Datum[] count : inp) {
					cn++;
				}
				Datum count = new Datum.Int(cn);
				tres[i] = count;
				break;
			case AVG:
				cn = 0;
				for (Datum[] avg : inp) {
					try {
						ans = ans + avg[index].toFloat();
						cn++;
					} catch (CastError e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Datum avg = new Datum.Flt(ans / cn);
				tres[i] = avg;
				break;
			case MIN:
				cn = 0;
				for (Datum[] min : inp) {
					try {
						if (cn == 0)
							ans = min[index].toFloat();
						else if (ans > min[index].toFloat())
							ans = min[index].toFloat();
						cn++;
					} catch (CastError e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Datum min = null;
				switch (c.type) {
				case INT:
					min = new Datum.Int((int) ans);
					break;
				case FLOAT:
					min = new Datum.Flt(ans);
					break;
				}
				tres[i] = min;
				break;
			case MAX:
				cn = 0;
				for (Datum[] max : inp) {
					try {
						if (cn == 0)
							ans = max[index].toFloat();
						if (ans < max[index].toFloat())
							ans = max[index].toFloat();
						cn++;
					} catch (CastError e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Datum max = null;
				switch (c.type) {
				case INT:
					max = new Datum.Int((int) ans);
					break;
				case FLOAT:
					max = new Datum.Flt(ans);
					break;
				}
				tres[i] = max;
				break;
			}

		}
		res.add(tres);
		return res;
	}
}

class Join {

	private Datum[] joinArray(Datum[] datum_arr1, Datum[] datum_arr2) {
		int tlength = datum_arr1.length + datum_arr2.length;
		Datum[] result = new Datum[tlength];
		for (int i = 0; i < tlength; i++) {
			if (i < datum_arr1.length)
				result[i] = datum_arr1[i];
			else
				result[i] = datum_arr2[i - datum_arr1.length];
		}
		return result;
	}

	public List<Datum[]> joinTables(List<Datum[]> left, List<Datum[]> right,
			JoinNode q, List<Schema.Var> vars) {
		List<Datum[]> res = new ArrayList<Datum[]>();
		vars.addAll(q.getSchemaVars());
		switch (q.getJoinType()) {
		case NLJ:
			int cnt = 0;
			for (Datum[] loop1 : right) {
				for (Datum[] loop2 : left) {
					res.add(joinArray(loop1, loop2));
					cnt++;
				}
			}

		}
		return res;
	}
}

class Select {
	EvaluateExpr eExpr = null;
	PlanNode q;

	public Select(PlanNode q) {
		this.q = q;
		eExpr = new EvaluateExpr();
	}

	public ArrayList<Datum[]> getNext(List<Datum[]> db) {
		ArrayList<Datum[]> pResult = new ArrayList<Datum[]>();
		Datum result = null;
		for (Datum[] loop1 : db) {
			if (eExpr != null) {
				eExpr.setTuple(loop1);
			}
			SelectionNode qNode = (SelectionNode) q;
			result = eExpr.eval(qNode.getCondition());
			try {
				if ((result.getType() == Schema.Type.BOOL)
						&& (result.toBool() == true)) {
					pResult.add(loop1);
				}
			} catch (CastError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return pResult;
	}
}

class EvaluateExpr {
	Datum[] tuple = null;
	int result;

	public EvaluateExpr() { // For Simple Evaluate eg: Select 1 + 2
	}

	/*
	 * public EvaluateExpr(Datum[] tuple){ this.tuple = tuple; }
	 */
	public void setTuple(Datum[] tuple) { // set tuple everytime in the loop
		this.tuple = tuple;
	}

	public Datum eval(ExprTree node) { // Get the results in Datum. Check type
										// if needed
		Datum result = null;
		Datum lhs = null;
		Datum rhs = null;
		switch (node.op) {
		case ADD:
			lhs = eval(node.get(0));
			rhs = eval(node.get(1));
			try {
				if (lhs.getType() == Schema.Type.FLOAT
						|| rhs.getType() == Schema.Type.FLOAT) {
					result = new Datum.Flt(lhs.toFloat() + rhs.toFloat());
				} else {
					result = new Datum.Int(
							(int) (rhs.toFloat() + lhs.toFloat()));
				}
			} catch (Datum.CastError e) {
			}
			break;
		case MULT:
			try {
				lhs = eval(node.get(0));
				rhs = eval(node.get(1));
				if (lhs.getType() == Schema.Type.FLOAT
						|| rhs.getType() == Schema.Type.FLOAT) {
					result = new Datum.Flt(lhs.toFloat() * rhs.toFloat());
				} else {
					result = new Datum.Int(
							(int) (rhs.toFloat() * lhs.toFloat()));
				}
			} catch (Datum.CastError e) {

			}
			break;
		case SUB:
			lhs = eval(node.get(0));
			rhs = eval(node.get(1));
			try {
				if (lhs.getType() == Schema.Type.FLOAT
						|| rhs.getType() == Schema.Type.FLOAT) {
					result = new Datum.Flt(lhs.toFloat() - rhs.toFloat());
				} else {
					result = new Datum.Int(
							(int) (rhs.toFloat() - lhs.toFloat()));
				}
			} catch (Datum.CastError e) {

			}
			break;
		case DIV:
			lhs = eval(node.get(0));
			rhs = eval(node.get(1));
			try {
				if (lhs.getType() == Schema.Type.FLOAT
						|| rhs.getType() == Schema.Type.FLOAT) {
					result = new Datum.Flt(lhs.toFloat() / rhs.toFloat());
				} else {
					result = new Datum.Int(
							(int) (rhs.toFloat() / lhs.toFloat()));
				}
			} catch (Datum.CastError e) {

			}
			break;
		case AND:
			lhs = eval(node.get(0));
			rhs = eval(node.get(1));
			try {
				result = new Datum.Bool(lhs.toBool() && rhs.toBool());
			} catch (Datum.CastError e) {

			}
			break;
		case OR:
			lhs = eval(node.get(0));
			rhs = eval(node.get(1));
			try {
				result = new Datum.Bool(lhs.toBool() || rhs.toBool());
			} catch (Datum.CastError e) {

			}
			break;
		case NOT:
			lhs = eval(node.get(0));
			try {
				result = new Datum.Bool(!lhs.toBool());
			} catch (Datum.CastError e) {

			}
			break;
		case EQ:
			lhs = eval(node.get(0));
			rhs = eval(node.get(1));
			try {
				if (lhs.getType() == Schema.Type.BOOL
						|| rhs.getType() == Schema.Type.BOOL) {
					result = new Datum.Bool(lhs.toBool() == rhs.toBool());
				} else if (lhs.getType() == Schema.Type.INT
						|| rhs.getType() == Schema.Type.INT) {
					result = new Datum.Bool(lhs.toInt() == rhs.toInt());
				} else if (lhs.getType() == Schema.Type.FLOAT
						|| rhs.getType() == Schema.Type.FLOAT) {
					result = new Datum.Bool(lhs.toFloat() == rhs.toFloat());
				} else if (lhs.getType() == Schema.Type.STRING
						|| rhs.getType() == Schema.Type.STRING) {
					result = new Datum.Bool(lhs.toString() == rhs.toString());
				}
			} catch (Datum.CastError e) {

			}
			break;
		case NEQ:
			lhs = eval(node.get(0));
			rhs = eval(node.get(1));
			try {
				if (lhs.getType() == Schema.Type.BOOL
						|| rhs.getType() == Schema.Type.BOOL) {
					result = new Datum.Bool(lhs.toBool() != rhs.toBool());
				} else if (lhs.getType() == Schema.Type.INT
						|| rhs.getType() == Schema.Type.INT) {
					result = new Datum.Bool(lhs.toInt() != rhs.toInt());
				} else if (lhs.getType() == Schema.Type.FLOAT
						|| rhs.getType() == Schema.Type.FLOAT) {
					result = new Datum.Bool(lhs.toFloat() != rhs.toFloat());
				} else if (lhs.getType() == Schema.Type.STRING
						|| rhs.getType() == Schema.Type.STRING) {
					result = new Datum.Bool(lhs.toString() != rhs.toString());
				}
			} catch (Datum.CastError e) {

			}
			break;
		case LT:
			lhs = eval(node.get(0));
			rhs = eval(node.get(1));
			try {
				if (lhs.getType() == Schema.Type.INT
						|| rhs.getType() == Schema.Type.INT) {
					result = new Datum.Bool(lhs.toInt() < rhs.toInt());
				} else if (lhs.getType() == Schema.Type.FLOAT
						|| rhs.getType() == Schema.Type.FLOAT) {
					result = new Datum.Bool(lhs.toFloat() < rhs.toFloat());
				} else if (lhs.getType() == Schema.Type.STRING
						|| rhs.getType() == Schema.Type.STRING) {
					if (lhs.toString().compareTo(rhs.toString()) < 0)
						result = new Datum.Bool(true);
					else
						result = new Datum.Bool(false);
				}
			} catch (Datum.CastError e) {

			}
			break;
		case GT:
			lhs = eval(node.get(0));
			rhs = eval(node.get(1));
			if (lhs.getType() == Schema.Type.INT
					|| rhs.getType() == Schema.Type.INT) {
				try {
					result = new Datum.Bool(lhs.toInt() > rhs.toInt());
				} catch (CastError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (lhs.getType() == Schema.Type.FLOAT
					|| rhs.getType() == Schema.Type.FLOAT) {
				try {
					result = new Datum.Bool(lhs.toFloat() > rhs.toFloat());
				} catch (CastError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (lhs.getType() == Schema.Type.STRING
					|| rhs.getType() == Schema.Type.STRING) {
				if (lhs.toString().compareTo(rhs.toString()) > 0)
					result = new Datum.Bool(true);
				else
					result = new Datum.Bool(false);
			}
			break;
		case LTE:
			lhs = eval(node.get(0));
			rhs = eval(node.get(1));
			if (lhs.getType() == Schema.Type.INT
					|| rhs.getType() == Schema.Type.INT) {
				try {
					result = new Datum.Bool(lhs.toInt() <= rhs.toInt());
				} catch (CastError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (lhs.getType() == Schema.Type.FLOAT
					|| rhs.getType() == Schema.Type.FLOAT) {
				try {
					result = new Datum.Bool(lhs.toFloat() <= rhs.toFloat());
				} catch (CastError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (lhs.getType() == Schema.Type.STRING
					|| rhs.getType() == Schema.Type.STRING) {
				if (lhs.toString().compareTo(rhs.toString()) <= 0)
					result = new Datum.Bool(true);
				else
					result = new Datum.Bool(false);
			}
			break;
		case GTE:
			lhs = eval(node.get(0));
			rhs = eval(node.get(1));
			if (lhs.getType() == Schema.Type.INT
					|| rhs.getType() == Schema.Type.INT) {
				try {
					result = new Datum.Bool(lhs.toInt() >= rhs.toInt());
				} catch (CastError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (lhs.getType() == Schema.Type.FLOAT
					|| rhs.getType() == Schema.Type.FLOAT) {
				try {
					result = new Datum.Bool(lhs.toFloat() >= rhs.toFloat());
				} catch (CastError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (lhs.getType() == Schema.Type.STRING
					|| rhs.getType() == Schema.Type.STRING) {
				if (lhs.toString().compareTo(rhs.toString()) >= 0)
					result = new Datum.Bool(true);
				else
					result = new Datum.Bool(false);
			}
			break;
		case CONST:
			ExprTree.ConstLeaf value = (ExprTree.ConstLeaf) node;
			result = value.v;
			break;
		case VAR:
			ExprTree.VarLeaf var = (ExprTree.VarLeaf) node;
			int idx = Project.getAttrIdx(var.toString());// check var.tString();
			result = tuple[idx];
			break;
		}
		return result;
	}
}
