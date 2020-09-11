package com.company.ai;

import com.company.model.Move;
import com.company.model.Sokoban;

import java.util.List;

public interface Solver
{

    List<Move> solve(final Sokoban sokoban);

}
