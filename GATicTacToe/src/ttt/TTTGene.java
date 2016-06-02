package ttt;

import ga.Allele;

/**
 * @author benji
 */
public class TTTGene extends Allele{
        private int targetCell;
        public TTTGene(){
            randomize();
        }
        public TTTGene(int targetCell){
            this.targetCell = targetCell;
        }
        @Override
        public void randomize(){
            targetCell = (int) (Math.random() * 9);
        }
        public int getTargetCell(){
            return targetCell;
        }
    }