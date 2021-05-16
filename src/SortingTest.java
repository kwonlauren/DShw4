import java.io.*;
import java.util.*;

public class SortingTest {
	public static void main(String args[]) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try {
			boolean isRandom = false;    // 입력받은 배열이 난수인가 아닌가?
			int[] value;    // 입력 받을 숫자들의 배열
			String nums = br.readLine();    // 첫 줄을 입력 받음
			if (nums.charAt(0) == 'r') {
				// 난수일 경우
				isRandom = true;    // 난수임을 표시

				String[] nums_arg = nums.split(" ");

				int numsize = Integer.parseInt(nums_arg[1]);    // 총 갯수
				int rminimum = Integer.parseInt(nums_arg[2]);    // 최소값
				int rmaximum = Integer.parseInt(nums_arg[3]);    // 최대값

				Random rand = new Random();    // 난수 인스턴스를 생성한다.

				value = new int[numsize];    // 배열을 생성한다.
				for (int i = 0; i < value.length; i++)    // 각각의 배열에 난수를 생성하여 대입
					value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
			} else {
				// 난수가 아닐 경우
				int numsize = Integer.parseInt(nums);

				value = new int[numsize];    // 배열을 생성한다.
				for (int i = 0; i < value.length; i++)    // 한줄씩 입력받아 배열원소로 대입
					value[i] = Integer.parseInt(br.readLine());
			}

			// 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
			while (true) {
				int[] newvalue = (int[]) value.clone();    // 원래 값의 보호를 위해 복사본을 생성한다.

				String command = br.readLine();

				long t = System.currentTimeMillis();
				switch (command.charAt(0)) {
					case 'B':    // Bubble Sort
						newvalue = DoBubbleSort(newvalue);
						break;
					case 'I':    // Insertion Sort
						newvalue = DoInsertionSort(newvalue);
						break;
					case 'H':    // Heap Sort
						newvalue = DoHeapSort(newvalue);
						break;
					case 'M':    // Merge Sort
						newvalue = DoMergeSort(newvalue);
						break;
					case 'Q':    // Quick Sort
						newvalue = DoQuickSort(newvalue);
						break;
					case 'R':    // Radix Sort
						newvalue = DoRadixSort(newvalue);
						break;
					case 'X':
						return;    // 프로그램을 종료한다.
					default:
						throw new IOException("잘못된 정렬 방법을 입력했습니다.");
				}
				if (isRandom) {
					// 난수일 경우 수행시간을 출력한다.
					System.out.println((System.currentTimeMillis() - t) + " ms");
				} else {
					// 난수가 아닐 경우 정렬된 결과값을 출력한다.
					for (int i = 0; i < newvalue.length; i++) {
						System.out.println(newvalue[i]);
					}
				}

			}
		} catch (IOException e) {
			System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoBubbleSort(int[] value) {
		// value는 정렬안된 숫자들의 배열이며 value.length 는 배열의 크기가 된다.
		// 결과로 정렬된 배열은 리턴해 주어야 하며, 두가지 방법이 있으므로 잘 생각해서 사용할것.
		// 주어진 value 배열에서 안의 값만을 바꾸고 value를 다시 리턴하거나
		// 같은 크기의 새로운 배열을 만들어 그 배열을 리턴할 수도 있다.
		int temp;
		for (int last = value.length - 1; last > 0; last--) {
			for (int i = 0; i < last; i++) {
				if (value[i] > value[i + 1]) {
					temp = value[i];
					value[i] = value[i + 1];
					value[i + 1] = temp;
				}
			}
		}
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoInsertionSort(int[] value) {
		for (int i = 1; i < value.length; i++) {
			int j = i - 1;
			int insertionItem = value[i];
			// value[0 ... i-1] is already sorted
			while (j > -1 && insertionItem < value[j]) {
				value[j + 1] = value[j];
				j--;
			}
			value[j + 1] = insertionItem;
		}
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoHeapSort(int[] value) {
		Heap h = new Heap(value);
		for (int i = value.length - 1; i > 0; i--) {
			value[i] = h.deleteMax();
		}
		return (value);
	}

	private static class Heap {
		private int[] A;
		private int numItems;

		public Heap(int[] B) {
			// array B(heap이 아닌 상태) heap으로 만들기.
			A = B;
			numItems = B.length;
			buildHeap();
		}

		public void buildHeap() {
			if (numItems >= 2) {
				for (int i = (numItems - 2) / 2; i > -1; i--) {
					percolateDown(i);
				}
			}
		}

		public int deleteMax() {
			if (!isEmpty()) {
				int max = A[0];
				A[0] = A[numItems - 1];
				numItems--;
				percolateDown(0);
				return max;
			} else return 0;
			//else throw new Exception("DeleteMax: empty heap");
		}

		public boolean isEmpty() {
			return numItems == 0;
		}

		private void percolateDown(int i) {
			//A[i]를 루트로 하여 percolate down
			int child = 2 * i + 1; //left child
			int rightChild = 2 * i + 2; //right child
			if (child <= numItems - 1) {
				if (rightChild <= numItems - 1 && A[child] < A[rightChild])
					child = rightChild; //child = index of larger child
				if (A[i] < A[child]) {
					int tmp = A[i];
					A[i] = A[child];
					A[child] = tmp;
					percolateDown(child);
				}
			}
		}

	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoMergeSort(int[] value) {
		int[] sub = new int[value.length];
		for (int i = 0; i < value.length; i++) {
			sub[i] = value[i];
		}
		mergeSort(value, sub, 0, value.length - 1);
		return (value);
	}

	private static void mergeSort(int[] A, int[] B, int p, int r) {
		//A[p ... r]을 정렬한다. B를 이용해서...
		if (p < r) {
			int q = (p + r) / 2;
			mergeSort(B, A, p, q);
			mergeSort(B, A, q + 1, r);
			merge(B, A, p, q, r);
		}
	}

	private static void merge(int[] A, int[] B, int p, int q, int r) {
		//A[p ... q], A[q+1 ... r]을 B[p ... r]에 merge
		int i = p;
		int j = q + 1;
		int t = p;
		while (i <= q && j <= r) {
			if (A[i] <= A[j]) B[t++] = A[i++];
			else B[t++] = A[j++];
		}
		while (i <= q) B[t++] = A[i++];
		while (j <= r) B[t++] = A[j++];
	}


	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoQuickSort(int[] value) {
		quickSort(value, 0, value.length - 1);
		return (value);
	}

	private static void quickSort(int[] A, int p, int r) {
		//sorts value[p ... r]
		if (p < r) {
			int q = partition(A, p, r);
			quickSort(A, p, q - 1);
			quickSort(A, q + 1, r);
		}
	}

	private static int partition(int[] A, int p, int r) {
		int x = A[r];
		int i = p - 1;//1구역의 끝
		for (int j = p; j < r; j++) {//j = 3구역의 시작
			if (A[j] <= x) {//1구역에 추가하는 경우
				int tmp = A[++i];
				A[i] = A[j];
				A[j] = tmp;
			}//나머지 경우에는 j만 증가시키면 됨
		}
		A[r] = A[i + 1];
		A[i + 1] = x;
		return i + 1;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoRadixSort(int[] value) {
		int[] sub = new int[value.length];
		for(int i=0; i<10; i++){//주종관계를 번갈아가며 i번째자리수를 기준으로 counting sort
			kth_countingSort(value, sub, i, value.length);
			kth_countingSort(sub, value, ++i, value.length);
		}
		return (value);
	}

	private static void kth_countingSort(int[] A, int[] B, int k, int n) {
		//k번째 자리수를 기준으로 stable sort (10^k 자리)
		//A를 가지고 B에다가 sorting 결과 저장
		int[] C = new int[20]; //C[i] = (i-10)의 개수. 음수면 자릿수로 비교할 때도 -붙이기
		for (int i = 0; i < 20; i++) {
			C[i] = 0;
		}
		for (int j = 0; j < n; j++) {
			int x = ((int)(A[j] / Math.pow(10,k)) % 10) +10;
			C[x]++;
		}
		for (int i=2; i<20; i++) C[i] = C[i]+C[i-1];
		for(int j=n-1; j>-1; j--){
			int x = ((int)(A[j] / Math.pow(10,k)) % 10) +10;
			B[C[x]-1] = A[j];
			C[x]--;
		}
	}
}