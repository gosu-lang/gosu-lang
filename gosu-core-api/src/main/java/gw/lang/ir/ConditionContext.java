package gw.lang.ir;

import gw.internal.ext.org.objectweb.asm.Label;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

public class ConditionContext
{
  private List<Label> _falseList;
  private List<Label> _trueList;
  private int _opcode;

  public ConditionContext()
  {
    _falseList = new ArrayList<>();
    _trueList = new ArrayList<>();
    _opcode = Opcodes.IFNE;
  }

  public Label generateFalseLabel()
  {
    Label label;
    if(_falseList.isEmpty()) {
      label = new Label();
      _falseList.add(label);
    } else {
      label = _falseList.get(0);
    }
    return label;
  }

  public Label generateTrueLabel()
  {
    Label label;
    if(_trueList.isEmpty()) {
      label = new Label();
      _trueList.add(label);
    } else {
      label = _trueList.get(0);
    }
    return label;
  }

  public void setFalseLabels( List<Label> labels )
  {
    _falseList = new ArrayList<>( labels );
  }

  public void setTrueLabels( List<Label> labels )
  {
    _trueList = new ArrayList<>( labels );
  }

  public void mergeLabels( boolean kind, ConditionContext context )
  {
    List<Label> labels = getLabels( kind );
    labels.addAll( context.getLabels( kind ) );
  }

  public void fixLabels(boolean kind, MethodVisitor mv)
  {
    List<Label> labels = getLabels(kind);
    for(Label l : labels)
    {
      mv.visitLabel( l );
    }
    labels.clear();
  }

  public List<Label> getLabels( boolean kind )
  {
    if(kind)
    {
      return _trueList;
    }
    else
    {
      return _falseList;
    }
  }

  public void update( ConditionContext context)
  {
    setFalseLabels( context.getLabels( false ) );
    setTrueLabels(  context.getLabels( true ) );
    _opcode = context.getOperator();
  }


  public void setOperator( int opcode )
  {
    _opcode = opcode;
  }

  public int getOperator()
  {
    return _opcode;
  }

  public void clear()
  {
    _falseList.clear();
    _trueList.clear();
    _opcode = Opcodes.IFNE;
  }
}
