package DSA.arr;

public class ArrayList {

    private int index = 0;
    int size = 5;
    int tempCopyArrSize = 0;
    private Object[] arr;

    public void add(Object n) {
        if (index >= size) {
            this.increaseCapacity();
        }
        arr[index] = n;
        index++;
    }

    public ArrayList() {
        arr = new Object[this.size];
    }

    public void increaseCapacity() {
        this.size = size + 3;
        tempCopyArrSize = arr.length;
        Object[] copyArr = arr;
        arr = new Object[this.size];
        for (int i = 0; i < arr.length; i++) {
            if (i < tempCopyArrSize) {
                arr[i] = copyArr[i];
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder arrStr = new StringBuilder("[");
        for (int i = 0; i < index; i++) {
            arrStr.append(arr[i]);
            if (i != index - 1) {
                arrStr.append(", ");
            }
        }
        arrStr.append("]");
        return arrStr.toString();
    }

    public Object length() {
        return index;
    }

    public int indexOf(Object val) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == val) {
                return i;
            }
        }
        return -1;
    }

    public void remove(int index) {

    }

}
