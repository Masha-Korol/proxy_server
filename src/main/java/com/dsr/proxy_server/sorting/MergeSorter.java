package com.dsr.proxy_server.sorting;

import com.dsr.proxy_server.data.entity.ProxyServer;
import com.dsr.proxy_server.data.enums.ProxyAnonymity;
import com.dsr.proxy_server.data.enums.ProxyServersSortingCriterion;
import org.springframework.stereotype.Service;

/**
 * This class performs merge sorting of an array of entities of ProxyServer by criteria of type ProxyServersSortingCriterion
 */
@Service
public class MergeSorter {

    public void sort(ProxyServer[] proxyServers, int count, ProxyServersSortingCriterion criterion) {
        mergeSort(proxyServers, count, criterion);
    }

    /**
     * This method compares two entities of ProxyServer, based on criterion
     *
     * @param proxyServer1 the first prosy server
     * @param proxyServer2 the second proxy server
     * @param criterion    criterion, by which the sorting should be done
     * @return boolean result
     */
    private boolean compare(ProxyServer proxyServer1,
                            ProxyServer proxyServer2,
                            ProxyServersSortingCriterion criterion) {
        switch (criterion) {
            case UPTIME:
                return proxyServer1.getUptime() > proxyServer2.getUptime();
            case ANONYMITY:
                Integer anonymityLevel1 = ProxyAnonymity.getAnonimityLevel(proxyServer1.getAnonymity());
                Integer anonymityLevel2 = ProxyAnonymity.getAnonimityLevel(proxyServer2.getAnonymity());
                return anonymityLevel1 < anonymityLevel2;
            case WORKLOAD:
                return proxyServer1.getWorkload() > proxyServer2.getWorkload();
        }
        return false;
    }

    /**
     * This method divides given array in two and recursively calls this method for both of them if their size is more than 1
     *
     * @param A         array to be divided
     * @param sizeA     array's size
     * @param criterion criterion, by which the sorting should be done
     */
    private void mergeSort(ProxyServer[] A, int sizeA, ProxyServersSortingCriterion criterion) {
        if (sizeA > 1) {
            ProxyServer[] B = new ProxyServer[sizeA / 2];
            ProxyServer[] C = new ProxyServer[sizeA - sizeA / 2];

            for (int i = 0; i < sizeA / 2; i++)
                B[i] = A[i];
            for (int i = sizeA / 2; i < sizeA; i++)
                C[i - sizeA / 2] = A[i];

            mergeSort(B, sizeA / 2, criterion);
            mergeSort(C, sizeA - sizeA / 2, criterion);
            merge(A, B, C, sizeA / 2, sizeA - sizeA / 2, criterion);
        }
    }

    /**
     * This method merges two given arrays into sorted one
     *
     * @param A         the first array
     * @param B         the second array
     * @param C         the third array - the one that will be the result of merging (as a result, the sorted one)
     * @param sizeB     size of the first array
     * @param sizeC     size of the second array
     * @param criterion criterion, by which the sorting should be done
     */
    private void merge(ProxyServer[] A, ProxyServer[] B, ProxyServer[] C,
                       int sizeB, int sizeC, ProxyServersSortingCriterion criterion) {
        int i = 0, j = 0, k = 0;
        while (i < sizeB && j < sizeC) {
            if (compare(C[j], B[i], criterion)) {
                A[k] = B[i];
                i++;
            } else {
                A[k] = C[j];
                j++;
            }
            k++;
        }
        if (i == sizeB)
            for (int t = j; t < sizeC; t++)
                A[k++] = C[t];
        else
            for (int t = i; t < sizeB; t++)
                A[k++] = B[t];
    }
}
