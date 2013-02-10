package edu.buffalo.cse.sql.test;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import edu.buffalo.cse.sql.Schema;
import edu.buffalo.cse.sql.data.Datum;
import edu.buffalo.cse.sql.plan.ExprTree;
import edu.buffalo.cse.sql.plan.PlanNode;
import edu.buffalo.cse.sql.plan.NullSourceNode;
import edu.buffalo.cse.sql.plan.ProjectionNode;
import edu.buffalo.cse.sql.plan.SelectionNode;
import edu.buffalo.cse.sql.plan.ScanNode;
import edu.buffalo.cse.sql.plan.JoinNode;
import edu.buffalo.cse.sql.plan.UnionNode;
import edu.buffalo.cse.sql.plan.AggregateNode;
public class AGG06 extends TestHarness {
  public static void main(String args[]) {
    TestHarness.main(args, new AGG06());
  }
  public void testRA() {
    Map<String, Schema.TableFromFile> tables
      = new HashMap<String, Schema.TableFromFile>();
    Schema.TableFromFile table_S;
    table_S = new Schema.TableFromFile(new File("test/r.dat"));
    table_S.add(new Schema.Column("S", "B", Schema.Type.INT));
    table_S.add(new Schema.Column("S", "C", Schema.Type.INT));
    tables.put("S", table_S);
    Schema.TableFromFile table_R;
    table_R = new Schema.TableFromFile(new File("test/r.dat"));
    table_R.add(new Schema.Column("R", "A", Schema.Type.INT));
    table_R.add(new Schema.Column("R", "B", Schema.Type.INT));
    tables.put("R", table_R);
    ScanNode lhs_2 = new ScanNode("R", "R", table_R);
    ScanNode rhs_3 = new ScanNode("S", "S", table_S);
    JoinNode child_1 = new JoinNode();
    child_1.setLHS(lhs_2);
    child_1.setRHS(rhs_3);
    AggregateNode query_0 = new AggregateNode();
    query_0.addAggregate(new AggregateNode.AggColumn("Count", new ExprTree.ConstLeaf(1),AggregateNode.AType.COUNT));
    query_0.setChild(child_1);
    TestHarness.testQuery(tables, getResults0(), query_0);
    System.out.println("Passed RA Test AGG06");
  }
  public void testSQL() {
    List<List<Datum[]>> expected = new ArrayList<List<Datum[]>>();
    expected.add(getResults0());
    TestHarness.testProgram(new File("test/AGG06.SQL"),
                            expected);
    System.out.println("Passed SQL Test AGG06");
  }
  ArrayList<Datum[]> getResults0() {
    ArrayList<Datum[]> ret = new ArrayList<Datum[]>();
    ret.add(new Datum[] {new Datum.Int(100)}); 
    return ret;
  }
}
