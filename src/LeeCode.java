public class LeeCode {
    /**
     * 给定一个二维网格和一个单词，找出该单词是否存在于网格中。
     * 单词必须按照字母顺序，通过相邻的单元格内的字母构成，其中“相邻”单元格是那些水平相邻或垂直相邻的单元格。
     * 同一个单元格内的字母不允许被重复使用
     * dsf
     */
    public boolean exist(char[][] board, String word) {
        boolean result;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == word.charAt(0)) {
                    result = dsf(board, word, i, j, 0);
                    if (result) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private static boolean dsf(char[][] board, String word, int i, int j, int index) {
        if (index == word.length()) {
            return true;
        }
        if (i < 0 || i >= board.length || j < 0 || j >= board[i].length) {
            return false;
        }
        if (board[i][j] != word.charAt(index)){
            return false;
        }

        boolean result;
        board[i][j] <<= 2;
        result =   dsf(board, word, i - 1, j, index + 1)
                || dsf(board, word, i + 1, j, index + 1)
                || dsf(board, word, i, j - 1, index + 1)
                || dsf(board, word, i, j + 1, index + 1);
        board[i][j] >>= 2;

        return result;
    }
    public static void main(String[] args) {
        char arr [][] = {{'A','B','C','E'},
                         {'S','F','C','S'},
                         {'A','D','E','E'},
                         {'A','B','C','D'}};
        LeeCode l  = new LeeCode();
        System.out.println(l.exist(arr, "ABCESEDCBAASFC"));
    }
}
