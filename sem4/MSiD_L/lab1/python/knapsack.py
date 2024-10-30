def unboundedKnapsack(W, wt, val, idx, dp):
    # Base Case
    # if we are at idx 0.
    if idx == 0:
        return (W // wt[0]) * val[0]
    # If the value is already calculated then we will
    # previous calculated value
    if dp[idx][W] != -1:
        return dp[idx][W]
    # There are two cases either take element or not take.
    # If not take then
    notTake = 0 + unboundedKnapsack(W, wt, val, idx - 1, dp)
    # if take then weight = W-wt[idx] and index will remain
    # same.
    take = float("-inf")
    if wt[idx] <= W:
        take = val[idx] + unboundedKnapsack(W - wt[idx], wt, val, idx, dp)
    dp[idx][W] = max(take, notTake)
    return dp[idx][W]


# Driver code
W = 12
val = [4, 2, 6, 5]
wt = [5, 3, 4, 8]
n = len(val)
dp = [[-1 for _ in range(W + 1)] for _ in range(n)]
print(unboundedKnapsack(W, wt, val, n - 1, dp))
for arr in dp:
    print(arr)
