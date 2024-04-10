from typing import List, Tuple


def get_confusion_matrix(
    y_true: List[int],
    y_pred: List[int],
    num_classes: int,
) -> List[List[int]]:
    """
    Generate a confusion matrix in a form of a list of lists.

    :param y_true: a list of ground truth values
    :param y_pred: a list of prediction values
    :param num_classes: number of supported classes

    :return: confusion matrix
    """
    ...


def get_quality_factors(
    y_true: List[int],
    y_pred: List[int],
) -> Tuple[int, int, int, int]:
    """
    Calculate True Negative, False Positive, False Negative and True Positive
    metrics basing on the ground truth and predicted lists.

    :param y_true: a list of ground truth values
    :param y_pred: a list of prediction values

    :return: a tuple of TN, FP, FN, TP
    """
    if len(y_true) != len(y_pred):
        raise ValueError("Lists must be the same size")

    TN, FP, FN, TP = 0, 0, 0, 0
    for true, pred in zip(y_true, y_pred):
        if true and pred:
            TP += 1
        elif not true and not pred:
            TN += 1
        elif true and not pred:
            FN += 1
        elif not true and pred:
            FP += 1
        else:
            raise ValueError("Lists should only contain true and false values")
    return TN, FP, FN, TP
    ...


def accuracy_score(y_true: List[int], y_pred: List[int]) -> float:
    """
    Calculate the accuracy for given lists.
    :param y_true: a list of ground truth values
    :param y_pred: a list of prediction values

    :return: accuracy score
    """
    ...


def precision_score(y_true: List[int], y_pred: List[int]) -> float:
    """
    Calculate the precision for given lists.
    :param y_true: a list of ground truth values
    :param y_pred: a list of prediction values

    :return: precision score
    """
    quality_factors = get_quality_factors(y_true, y_pred)
    TP, FP = quality_factors[3], quality_factors[1]
    return TP / (TP + FP)
    ...


def recall_score(y_true: List[int], y_pred: List[int]) -> float:
    """
    Calculate the recall for given lists.
    :param y_true: a list of ground truth values
    :param y_pred: a list of prediction values

    :return: recall score
    """
    ...


def f1_score(y_true: List[int], y_pred: List[int]) -> float:
    """
    Calculate the F1-score for given lists.
    :param y_true: a list of ground truth values
    :param y_pred: a list of prediction values

    :return: F1-score
    """
    ...
