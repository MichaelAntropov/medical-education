class MediaItem {
    id;
    testId;
    name;
    type;
    src;

    constructor(id, testId, name, type, src) {
        this.id = id;
        this.testId = testId;
        this.name = name;
        this.type = type;
        this.src = src;
    }
}

class Question {
    id;
    testId;
    orderNumber;
    content;
    edited;
    answers;

    constructor(id, testId, orderNumber, content, isEdited, answers) {
        this.id = id;
        this.testId = testId;
        this.orderNumber  = orderNumber;
        this.content = content;
        this.edited = isEdited;
        this.answers = answers;
    }
}

class Answer {
    id;
    questionId;
    orderNumber;
    correct;
    content;

    constructor(id, questionId, orderNumber, isCorrect, content) {
        this.id = id;
        this.questionId = questionId;
        this.orderNumber = orderNumber;
        this.correct = isCorrect;
        this.content = content;
    }
}

class TestDataPostRequestJson {
    testId;
    questions;
    questionsToDelete;
    answersToDelete;

    constructor(testId, questions, questionsToDelete, answersToDelete) {
        this.testId = testId;
        this.questions = questions;
        this.questionsToDelete = questionsToDelete;
        this.answersToDelete = answersToDelete;
    }
}

class MediaResponseJson {
    message;
    mediaItems = [];

    constructor(message) {
        this.message = message;
    }
}

class TestDataResponseJson {
    message;

    constructor(message) {
        this.message = message;
    }
}

const host = window.location.origin;

const mediaList = [];
const mainMediaListElement = document.getElementById("mainMediaList");
const questionMediaListElement = document.getElementById("questionModalMediaList");
const answerMediaListElement = document.getElementById("answerModalMediaList");

const courseId = document.getElementById("courseId").value;
const testId = document.getElementById("id").value;
const csrfHeader = document.getElementsByName("_csrf_header").item(0).value;
const csrf = document.getElementsByName("_csrf").item(0).value;

const onLoadMediaData = receiveMediaList();


//Functions for main info alert
/////////////////////////////////////////////////
function mainInfoShowAlert(message) {
    let mainInfoAlertsElement = document.getElementById("mainInfoAlertsElement");
    let alertId = getRandomInt(0, 2147483647);
    let alert = createInfoAlertNode(message, alertId);
    mainInfoAlertsElement.appendChild(alert);
}

function createInfoAlertNode(message, alertId) {
    return htmlToElement(
        "<div id=\"mainInfoAlert" + alertId + "\" class=\"alert alert-primary alert-dismissible fade show\" role=\"alert\"\n" +
        "     >\n" +
        "    <div id=\"mainInfoAlertText\">" + message + "</div>\n" +
        "    <button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\" aria-label=\"Close\">\n" +
        "        <span aria-hidden=\"true\">&times;</span>\n" +
        "    </button>\n" +
        "</div>"
    );
}


///////////////
///////////////
///////////////
//Functions for media
/////////////////////////////////////////////////
let imageToUpload = null;
let mediaToDelete = null;

function getMediaItemById(mediaId) {
    return mediaList.find(mediaItem => mediaItem.id === mediaId);
}

function getMediaItemNodesById(mediaId) {
    let className = "mediaItemNode" + mediaId;
    return document.getElementsByClassName(className);
}

function addMediaToLists(mediaItem) {
    mediaList.push(mediaItem);
    let mediaItemNode = getImageNode(mediaItem);
    mainMediaListElement.appendChild(mediaItemNode.cloneNode(true));
    questionMediaListElement.appendChild(mediaItemNode.cloneNode(true));
    answerMediaListElement.appendChild(mediaItemNode.cloneNode(true));
}

function removeMediaFromLists(mediaItem) {
    //Remove itemMedia from list and nodeList
    let mediaItemIndex;
    for (let i = 0; i < mediaList.length; i++) {
        let item = mediaList[i];
        if (item.id === mediaItem.id) {
            mediaItem = item;
            mediaItemIndex = i;
            break;
        }
    }
    mediaList.splice(mediaItemIndex, 1);
    let nodes = getMediaItemNodesById(mediaItem.id);
    mainMediaListElement.removeChild(nodes[0]);
    questionMediaListElement.removeChild(nodes[0]);
    answerMediaListElement.removeChild(nodes[0]);
    //Clear delete element
    mediaToDelete = null;
}

function getImageNode(mediaItem) {
    return htmlToElement(
        "<div class=\"mediaItemNode" + mediaItem.id + " card pt-3 mt-3\">\n" +
        "    <div class=\"row mb-3\">\n" +
        "        <div class=\"col ml-2\">\n" +
        "            <img src=\"" + mediaItem.src + "\"\n" +
        "                 alt=\"Image not found\"\n" +
        "                 class=\"img-thumbnail mr-2\"\n" +
        "                 style=\"height: 100px\"/>\n" +
        "            <p>" + mediaItem.name + "</p>\n" +
        "           <button class=\"btn btn-sm btn-outline-info mr-1 mt-1\" type=\"button\" " +
        "                    onclick=\"copyUrl('" + mediaItem.src + "', this)\">\n" +
        "                Copy link to clipboard\n" +
        "            </button>\n" +
        "            <button class=\"btn btn-sm btn-outline-danger mr-1 mt-1\" type=\"button\"\n" +
        "                    onclick=\"openDeleteMediaModal(" + mediaItem.id + ")\">\n" +
        "                <i class=\"fas fa-trash-alt\" style=\"font-size: 1rem\"></i>\n" +
        "            </button>\n" +
        "       </div>\n" +
        "    </div>\n" +
        "</div>"
    );
}

function openDeleteMediaModal(mediaItemId) {
    mediaToDelete = getMediaItemById(mediaItemId);

    document.getElementById("deleteMediaModalText").innerText =
        "Are you sure you want to delete this element: " + mediaToDelete.name + "?";

    $('#deleteMediaModal').modal();
}

function readImage(input) {
    document.getElementById("imageThumbnail").hidden = false;

    if (input.files && input.files[0]) {
        let reader = new FileReader();

        reader.onload = function (e) {
            let thumbnail = document.getElementById("imageThumbnail");
            thumbnail.getAttribute("src")
            $('#imageThumbnail')
                .attr('src', e.target.result);
        };

        reader.readAsDataURL(input.files[0]);

        document.getElementById("imageName").textContent = input.files[0].name;

        imageToUpload = input.files[0];
    }
}

async function receiveMediaList() {

    let mediaResponseJson;

    try {
        let response = await
            fetch(`${host}/user-dashboard/manage-courses/course${courseId}/lessons/test${testId}/images`, {
                method: "GET"
            });

        if (!response.ok) {
            mainInfoShowAlert("Error - " + response.status + ".")
            return;
        }

        mediaResponseJson = await response.json();
    } catch (err) {
        mediaResponseJson = new MediaResponseJson(err.message);
        mainInfoShowAlert(mediaResponseJson.message);
        return;
    }

    mainInfoShowAlert(mediaResponseJson.message);

    //Clear media list
    for (let i = 0; i < mediaList.length; i++) {
        mediaList.pop();
    }

    let list = mediaResponseJson.mediaItems;
    //Clear media container
    mainMediaListElement.innerHTML = "";
    questionMediaListElement.innerHTML = "";
    answerMediaListElement.innerHTML = "";
    //Fill media container
    list.forEach(item => {
        mediaList.push(item);
        let imageNode = getImageNode(item);
        mainMediaListElement.appendChild(imageNode.cloneNode(true));
        questionMediaListElement.appendChild(imageNode.cloneNode(true));
        answerMediaListElement.appendChild(imageNode.cloneNode(true));
    })
}

async function uploadImage() {
    if (imageToUpload == null) {
        //Set message info
        mainInfoShowAlert("Image is empty.")
        return;
    }

    let mediaResponseJson;

    try {
        let data = new FormData();
        data.append('testId', testId);
        data.append('image', imageToUpload);
        data.append('_csrf', csrf);

        let response = await fetch(`${host}/user-dashboard/manage-courses/course/lessons/test/image/upload`, {
                method: "POST",
                body: data
            });

        if (!response.ok) {
            mainInfoShowAlert("Error - " + response.status + ".")
            return;
        }

        mediaResponseJson = await response.json();
    } catch (err) {
        mediaResponseJson = new MediaResponseJson(err.message);
        mainInfoShowAlert(mediaResponseJson.message);
        return;
    }
    //Add new image to the media lists
    mainInfoShowAlert(mediaResponseJson.message);
    addMediaToLists(mediaResponseJson.mediaItems[0]);
}

async function deleteImage() {
    if (mediaToDelete == null) {
        //Set message info
        mainInfoShowAlert("Could not delete image. Image is not chosen.")
        return;
    }

    let mediaResponseJson;

    try {
        let data = new FormData();
        data.append('imageId', mediaToDelete.id);
        data.append('_csrf', csrf);

        let response = await fetch(`${host}/user-dashboard/manage-courses/course/lessons/test/image/delete`, {
            method: "POST",
            body: data
        });

        if (!response.ok) {
            mainInfoShowAlert("Error - " + response.status + ".")
            return;
        }

        mediaResponseJson = await response.json();
    } catch (err) {
        mediaResponseJson = new MediaResponseJson(err.message);
        mainInfoShowAlert(mediaResponseJson.message);
        return;
    }

    //Request was successful, now remove the element from lists
    mainInfoShowAlert(mediaResponseJson.message);
    removeMediaFromLists(mediaToDelete);
}

///////////////
///////////////
///////////////
//Question/answer functions
////////////////////////////////////////////////////
const testQuestions = [];

const testQuestion = [];

const testQuestionsElement = document.getElementById("questionList");

const testData = new TestDataPostRequestJson(parseInt(testId), [], [], []);

const onLoadTestData = receiveTestData();

function getMainQuestionNodeByOrderNumber(questionOrderNumber) {
    return document.getElementById("questionNode" + questionOrderNumber);
}

function getQuestionAnswerNodeByOrderNumber(answerOrderNumber) {
    return document.getElementById("questionModalAnswerNode" + answerOrderNumber);
}

function getMainQuestionNode(question) {
    let upDisabled = question.orderNumber === 1 ? "disabled=\"disabled\"" : "";
    let downDisabled = question.orderNumber === testQuestions.length ? "disabled=\"disabled\"" : "";
    return htmlToElement(
        "<div id=\"questionNode" + question.orderNumber + "\" class=\"card card-body mb-2\">\n" +
        "    <div class=\"row\">\n" +
        "        <div class=\"col-12 overflow\">" + question.content + "</div>\n" +
        "        <div class=\"row\">\n" +
        "            <div class=\"col\">\n" +
        "                <button type=\"button\" class=\"btn btn-danger float-right m-1\" onclick=\"showDeleteQuestionModal(" + question.orderNumber + ")\" data-toggle=\"modal\" data-target=\"#deleteModal\">\n" +
        "                    Delete\n" +
        "                </button>\n" +
        "                <button type=\"button\" class=\"btn btn-primary float-right m-1\" onclick=\"showEditQuestion(" + question.orderNumber + ")\">\n" +
        "                    Edit\n" +
        "                </button>\n" +
        "                <button type=\"button\" class=\"btn btn-light btn-outline-info float-right m-1\" onclick=\"moveQuestionDown(" + question.orderNumber + ")\" " + downDisabled + ">\n" +
        "                    &#9660;\n" +
        "                </button>\n" +
        "                <button type=\"button\"  class=\"btn btn-light btn-outline-info float-right m-1\" onclick=\"moveQuestionUp(" + question.orderNumber + ")\" " + upDisabled + ">\n" +
        "                    &#9650;\n" +
        "                </button>\n" +
        "            </div>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "</div>"
    );
}

function getQuestionAnswerNode(answer) {
    let checked = answer.correct ? "checked=\"checked\"" : "";
    let upDisabled = answer.orderNumber === 1 ? "disabled=\"disabled\"" : "";
    let downDisabled = answer.orderNumber === testQuestion[0].answers.length ? "disabled=\"disabled\"" : "";
    return htmlToElement(
        "<div id=\"questionModalAnswerNode" + answer.orderNumber + "\" class=\"card card-body mb-3\">\n" +
        "    <div class=\"row\">\n" +
        "        <div class=\"col-12\">" + answer.content + "</div>\n" +
        "        <div class=\"row\">\n" +
        "            <div class=\"col\">\n" +
        "                <button type=\"button\" class=\"btn btn-danger float-right m-1\" onclick=\"showDeleteAnswerModal(" + answer.orderNumber + ")\" data-toggle=\"modal\" data-target=\"#deleteModal\">\n" +
        "                    Delete\n" +
        "                </button>\n" +
        "                <button type=\"button\" class=\"btn btn-primary float-right m-1\" onclick=\"showEditAnswer(" + answer.orderNumber +  ")\">\n" +
        "                    Edit\n" +
        "                </button>\n" +
        "                <button type=\"button\" class=\"btn btn-light btn-outline-info float-right m-1\" onclick=\"moveAnswerDown(" + answer.orderNumber + ")\" " + downDisabled + ">\n" +
        "                    &#9660;\n" +
        "                </button>\n" +
        "                <button type=\"button\" class=\"btn btn-light btn-outline-info float-right m-1\" onclick=\"moveAnswerUp(" + answer.orderNumber + ")\" " + upDisabled + ">\n" +
        "                    &#9650;\n" +
        "                </button>\n" +
        "                <div class=\"custom-control custom-radio float-right m-1 mt-2\">\n" +
        "                    <input class=\"custom-control-input\" type=\"radio\" " + checked + " onclick=\"checkAsCorrectAnswer(" + answer.orderNumber + ")\" \n" +
        "                           id=\"answerRadioButton" + answer.orderNumber + "\" name=\"answer\">\n" +
        "                        <label class=\"custom-control-label\" for=\"answerRadioButton" + answer.orderNumber + "\">\n" +
        "                            Correct\n" +
        "                        </label>\n" +
        "                </div>\n" +
        "            </div>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "</div>"
    );
}

function addAnswerNode(answer) {
    document.getElementById("questionModalAnswerList")
        .appendChild(getQuestionAnswerNode(answer));
}

function addQuestionNode(question) {
    document.getElementById("questionList")
        .appendChild(getMainQuestionNode(question));
}

///////////////

function showCreateQuestion() {
    $("#questionModal").modal('show');
    testQuestion[0] = new Question(null, parseInt(testId), null, "", true, []);
    prepareQuestionContentCreate();
}

function saveCreatedQuestion() {
    let question = testQuestion[0];
    question.orderNumber = testQuestions.length + 1;
    question.content = getQuestionContent();
    question.edited = true;
    testQuestions.push(question);
    addQuestionNode(question);
    testQuestion.pop();
    redrawQuestions();
    $("#questionModal").modal('hide');
}

function showEditQuestion(questionOrderNumber) {
    $("#questionModal").modal('show');
    let reference = testQuestions.find(value => {
        return value.orderNumber === questionOrderNumber;
    });
    testQuestion[0] = JSON.parse(JSON.stringify(reference));
    testQuestion[0].edited = true;
    prepareQuestionContentEdit();
}

function saveEditedQuestion() {
    let questionEdited = testQuestion[0];
    questionEdited.content = getQuestionContent();
    //Save to main list
    testQuestions[questionEdited.orderNumber - 1] = questionEdited;
    //Update ui
    let node = getMainQuestionNodeByOrderNumber(questionEdited.orderNumber);
    node.replaceWith(getMainQuestionNode(questionEdited));
    $("#questionModal").modal('hide');
}

function deleteQuestion(orderNumber) {
    //Add this to array of deleted questions if it exists on server
    if(testQuestions[orderNumber - 1].id !== null) {
        testData.questionsToDelete.push(testQuestions[orderNumber - 1].id);
    }
    testQuestions.splice(orderNumber - 1, 1);
    testQuestions.forEach((question, index) => {
        question.orderNumber = index + 1;
    })
    redrawQuestions();
}

function moveQuestionUp(orderNumber) {
    if(orderNumber === 1) {
        return;
    }
    testQuestions[orderNumber - 1].orderNumber = orderNumber - 1;
    testQuestions[orderNumber - 2].orderNumber = orderNumber;

    testQuestions.sort((question1, question2) => {
        return question1.orderNumber - question2.orderNumber;
    })
    redrawQuestions();
}

function moveQuestionDown(orderNumber) {
    if(orderNumber === testQuestions.length) {
        return;
    }
    testQuestions[orderNumber - 1].orderNumber = orderNumber + 1;
    testQuestions[orderNumber].orderNumber = orderNumber;

    testQuestions.sort((question1, question2) => {
        return question1.orderNumber - question2.orderNumber;
    })
    redrawQuestions();
}

function showDeleteQuestionModal(orderNumber) {
    document.getElementById("deleteModalTitle")
        .innerText = "Delete question";
    document.getElementById("deleteModalText")
        .innerText = "Are you sure you want to delete this question?";
    document.getElementById("deleteModalActionButton").onclick = function () {
        deleteQuestion(orderNumber);
    }
}

function closeQuestionWarning() {
    $("#warningModal").modal('hide');
    $("#questionModal").modal('hide');
}

////////////////

function showCreateAnswer() {
    $("#answerModal").modal('show');
    prepareAnswerContentCreate();
}

function saveCreatedAnswer() {
    let question = testQuestion[0];
    let answer = new Answer();
    answer.id = null;
    answer.questionId = null;
    answer.orderNumber = question.answers.length + 1;
    answer.content = getAnswerContent();
    answer.correct = false;
    answer.edited = true;
    question.answers.push(answer);
    redrawAnswers();
    $("#answerModal").modal('hide');
}

function showEditAnswer(answerOrderNumber) {
    $("#answerModal").modal('show');
    let answer = testQuestion[0].answers.find(value => {
        return value.orderNumber === answerOrderNumber;
    });
    answer.edited = true;
    prepareAnswerContentEdit(answer);
}

function saveEditedAnswer(answer) {
    answer.content = getAnswerContent();
    getQuestionAnswerNodeByOrderNumber(answer.orderNumber)
        .replaceWith(getQuestionAnswerNode(answer));
    $("#answerModal").modal('hide');
}

function deleteAnswer(orderNumber) {
    //Add this to array of deleted answers if it exists on server
    let answerId = orderNumber - 1;
    if(testQuestion[0].answers[answerId].id !== null) {
        testData.answersToDelete.push(testQuestion[0].answers[answerId].id);
    }
    testQuestion[0].answers.splice(orderNumber - 1, 1);
    testQuestion[0].answers.forEach((answer, index) => {
        answer.orderNumber = index + 1;
    })
    redrawAnswers();

}

function moveAnswerUp(orderNumber) {
    if(orderNumber === 1) {
        return;
    }
    testQuestion[0].answers[orderNumber - 1].orderNumber = orderNumber - 1;
    testQuestion[0].answers[orderNumber - 2].orderNumber = orderNumber;

    testQuestion[0].answers.sort((answer1, answer2) => {
        return answer1.orderNumber - answer2.orderNumber;
    });
    redrawAnswers();
}

function moveAnswerDown(orderNumber) {
    if(orderNumber === testQuestion[0].answers.length) {
        return;
    }
    testQuestion[0].answers[orderNumber - 1].orderNumber = orderNumber + 1;
    testQuestion[0].answers[orderNumber].orderNumber = orderNumber;

    testQuestion[0].answers.sort((answer1, answer2) => {
        return answer1.orderNumber - answer2.orderNumber;
    });
    redrawAnswers();
}

function showDeleteAnswerModal(orderNumber) {
    document.getElementById("deleteModalTitle")
        .innerText = "Delete answer";
    document.getElementById("deleteModalText")
        .innerText = "Are you sure you want to delete this answer?";
    document.getElementById("deleteModalActionButton").onclick = function () {
        deleteAnswer(orderNumber);
    }
}

function closeAnswerWarning() {
    $("#warningModal").modal('hide');
    $("#answerModal").modal('hide');
}

function questionModalMediaButton() {
    $("#questionModalMediaCollapse").collapse('toggle');
    let button = document.getElementById("questionModalMediaButton");
    if(button.innerText === "Show media") {
        button.innerText = "Hide media";
    } else {
        button.innerText = "Show media";
    }
}

function answerModalMediaButton() {
    $("#answerModalMediaCollapse").collapse('toggle');
    let button = document.getElementById("answerModalMediaButton");
    if(button.innerText === "Show media") {
        button.innerText = "Hide media";
    } else {
        button.innerText = "Show media";
    }
}

function checkAsCorrectAnswer(answerOrderNumber) {
    let question = testQuestion[0];
    question.answers.forEach(answer => {
        answer.correct = (answer.orderNumber === answerOrderNumber);
    })
}

/////////////

function prepareQuestionContentCreate() {
    document.getElementById("questionModalTitle").innerText = "Create question";
    document.getElementById("questionModalAnswerList").innerHTML = "";
    setQuestionContent("");
    document.getElementById("questionModalActionButton").innerText = "Create";

    //On close
    document.getElementById("questionModalCloseButton").onclick = function () {
        $("#warningModal").modal();
        document.getElementById("warningModalTitle").innerText = "Close create question";
        document.getElementById("warningModalText").innerText =
            "Are you sure you want to close this? The question will not be saved!";
        document.getElementById("warningModalCloseButton").onclick = function () {
            closeQuestionWarning();
        };
    }
    //On create
    document.getElementById("questionModalActionButton").onclick = function () {
        saveCreatedQuestion();
    }
}

function prepareQuestionContentEdit() {
    let question = testQuestion[0];
    document.getElementById("questionModalTitle").innerText = "Edit question";
    document.getElementById("questionModalAnswerList").innerHTML = "";
    //Set content
    setQuestionContent(question.content);
    //Set answers
    question.answers.forEach(answer => {
        addAnswerNode(answer);
    })
    document.getElementById("questionModalActionButton").innerText = "Save";
    //On close
    document.getElementById("questionModalCloseButton").onclick = function () {
        $("#warningModal").modal();
        document.getElementById("warningModalTitle").innerText = "Close edit question";
        document.getElementById("warningModalText").innerText =
            "Are you sure you want to close this? The question changes will not be saved!";
        document.getElementById("warningModalCloseButton").onclick = function () {
            closeQuestionWarning();
        };
    }
    //On edit
    document.getElementById("questionModalActionButton").onclick = function () {
        saveEditedQuestion();
    }
}

function prepareAnswerContentCreate() {
    document.getElementById("answerModalTitle").innerText = "Create answer";
    setAnswerContent("");
    document.getElementById("answerModalActionButton").innerText = "Create";

    //On close
    document.getElementById("answerModalCloseButton").onclick = function () {
        $("#warningModal").modal();
        document.getElementById("warningModalTitle").innerText = "Close create answer";
        document.getElementById("warningModalText").innerText =
            "Are you sure you want to close this? The answer will not be saved!";
        document.getElementById("warningModalCloseButton").onclick = function () {
            closeAnswerWarning();
        };
    }
    //On sve
    document.getElementById("answerModalActionButton").onclick = function () {
        saveCreatedAnswer();
    };
}

function prepareAnswerContentEdit(answer) {
    document.getElementById("answerModalTitle").innerText = "Edit answer";
    setAnswerContent(answer.content);
    document.getElementById("answerModalActionButton").innerText = "Save";

    //On close
    document.getElementById("answerModalCloseButton").onclick = function () {
        $("#warningModal").modal();
        document.getElementById("warningModalTitle").innerText = "Close edit answer";
        document.getElementById("warningModalText").innerText =
            "Are you sure you want to close this? The answer changes will not be saved!";
        document.getElementById("warningModalCloseButton").onclick = function () {
            closeAnswerWarning();
        };
    }
    //On save
    document.getElementById("answerModalActionButton").onclick = function () {
        saveEditedAnswer(answer);
    };
}

function redrawQuestions() {
    //Clear questions container
    testQuestionsElement.innerHTML = "";
    //Fill questions container
    testQuestions.forEach(question => {
        let questionNode = getMainQuestionNode(question);
        testQuestionsElement.appendChild(questionNode);
    });
}

function redrawAnswers() {
    //Clear questions container
    let answerListElement = document.getElementById("questionModalAnswerList");
    answerListElement.innerText = "";
    //Fill questions container
    testQuestion[0].answers.forEach(answer => {
        let answerNode = getQuestionAnswerNode(answer);
        answerListElement.appendChild(answerNode);
    });
}

function clearArrays() {
    testQuestions.length = 0;
    testQuestion.length = 0;
    testData.questions = 0;
    testData.questionsToDelete.length = 0
    testData.answersToDelete.length = 0
}

function questionModalAnswerButton() {
    showCreateAnswer();
}

function setQuestionContent(content) {
    tinymce.get("questionModalContent").setContent(content);
}

function getQuestionContent() {
    return tinymce.get("questionModalContent").getContent();
}

function setAnswerContent(content) {
    tinymce.get("answerModalContent").setContent(content);
}

function getAnswerContent() {
    return tinymce.get("answerModalContent").getContent();
}

async function receiveTestData() {

    clearArrays();

    let testDataResponseJson;

    try {
        let response = await
            fetch(`${host}/user-dashboard/manage-courses/course${courseId}/lessons/test${testId}/test-data`, {
                method: "GET"
            });

        if (!response.ok) {
            mainInfoShowAlert("Error - " + response.status + ".")
            return;
        }

        testDataResponseJson = await response.json();
    } catch (err) {
        testDataResponseJson = new MediaResponseJson(err.message);
        mainInfoShowAlert(testDataResponseJson.message);
        return;
    }

    mainInfoShowAlert(testDataResponseJson.message);

    let list = testDataResponseJson.questions;

    list.forEach(question => {
        testQuestions.push(question);
    })

    testQuestions.sort((question1, question2) => {
        return question1.orderNumber - question2.orderNumber;
    })
    //Do it separately to allow ui draw correctly
    redrawQuestions();
}

async function postTestData() {

    testData.questions = testQuestions;

    let testDataResponseJson;

    try {
        let data = JSON.stringify(testData);


        let response = await fetch(`${host}/user-dashboard/manage-courses/course/lessons/test/test-data/post`, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN' : csrf,
            },
            body: data
        });

        if (!response.ok) {
            mainInfoShowAlert("Error - " + response.status + ".")
            return;
        }

        testDataResponseJson = await response.json();
    } catch (err) {
        testDataResponseJson = new TestDataResponseJson(err.message);
        mainInfoShowAlert(testDataResponseJson.message);
        return;
    }
    mainInfoShowAlert(testDataResponseJson.message);

    await receiveTestData();
}
///////////////
///////////////
///////////////
//Utility functions
////////////////////////////////////////////////////
//src - what to copy, node - where an temp input will be added
function copyUrl(src, node) {

    let copyText = document.createElement('input');
    node.appendChild(copyText);

    copyText.type = 'text';
    copyText.value = src;
    copyText.select();
    copyText.setSelectionRange(0, 99999); /*For mobile devices*/
    document.execCommand("copy");
    copyText.type = 'hidden';
    copyText.remove();
}

function htmlToElement(html) {
    let template = document.createElement('template');
    html = html.trim(); // Never return a text node of whitespace as the result
    template.innerHTML = html;
    return template.content.firstChild;
}

function getRandomInt(min, max) {
    return Math.random() * (max - min) + min;
}

function logTestData() {
    testData.questions = testQuestions;
    console.log(testData);
}

function logArray() {
    console.log(testQuestions);
}
function postData() {
    postTestData();
}
