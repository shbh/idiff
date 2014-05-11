package com.image.diff.helper;

import com.image.diff.ImagesContainer;
import com.image.diff.core.MatchContext;
import com.image.diff.core.TemplateMatchMethod;
import java.io.File;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FinderArgumentValidationTest {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static ImagesContainer imagesContainer;

    @BeforeClass
    public static void beforeTest() {
        imagesContainer = new ImagesContainer();
    }

    @AfterClass
    public static void afterTest() {
        imagesContainer = null;
    }

    @Test
    public void testFinderBuilderValidationsWithMatchContextBuilder() throws Exception {
        // try context as null value
        try {
            Finder finder = new Finder.Builder(null).build();
            Assert.fail("NullPointerException expected");
        } catch (NullPointerException e) {
        }

        // try empty context
        try {
            MatchContext context = new MatchContext.Builder().build();
            Finder finder = new Finder.Builder(context).build();
            Assert.fail("NullPointerException expected");
        } catch (NullPointerException e) {
        }

        // try context with image1 field only
        try {
            MatchContext context = new MatchContext.Builder().
                image1(imagesContainer.getDiffImage1File()).
                build();
            Finder finder = new Finder.Builder(context).build();
            Assert.fail("NullPointerException expected");
        } catch (NullPointerException e) {
        }

        // try context with image1 and image2 fields only
        try {
            MatchContext context = new MatchContext.Builder().
                image1(imagesContainer.getDiffImage1File()).
                image2(imagesContainer.getDiffImage2File()).
                build();
            Finder finder = new Finder.Builder(context).build();
            Assert.fail("NullPointerException expected");
        } catch (NullPointerException e) {
        }

        // try context with image1, image2 and resultImage fields only
        try {
            MatchContext context = new MatchContext.Builder().
                image1(imagesContainer.getDiffImage1File()).
                image2(imagesContainer.getDiffImage2File()).
                resultImage(imagesContainer.getResultImageFile()).
                build();
            Finder finder = new Finder.Builder(context).build();
            Assert.fail("NullPointerException expected");
        } catch (NullPointerException e) {
        }

        // try context with image1, image2, resultImage and resultSourceImage fields only
        try {
            MatchContext context = new MatchContext.Builder().
                image1(imagesContainer.getDiffImage1File()).
                image2(imagesContainer.getDiffImage2File()).
                resultImage(imagesContainer.getResultImageFile()).
                resultSourceImage(imagesContainer.getResultSourceImageFile()).
                build();
            Finder finder = new Finder.Builder(context).build();
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        }

        // try context with image1, image2, resultImage, resultSourceImage and negative matchSimilarity fields only
        try {
            MatchContext context = new MatchContext.Builder().
                image1(imagesContainer.getDiffImage1File()).
                image2(imagesContainer.getDiffImage2File()).
                resultImage(imagesContainer.getResultImageFile()).
                resultSourceImage(imagesContainer.getResultSourceImageFile()).
                matchSimilarity(-0.1D).
                build();
            Finder finder = new Finder.Builder(context).build();
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        }

        // try context with image1, image2, resultImage, resultSourceImage and matchSimilarity > 1 fields only
        try {
            MatchContext context = new MatchContext.Builder().
                image1(imagesContainer.getDiffImage1File()).
                image2(imagesContainer.getDiffImage2File()).
                resultImage(imagesContainer.getResultImageFile()).
                resultSourceImage(imagesContainer.getResultSourceImageFile()).
                matchSimilarity(1.1D).
                build();
            Finder finder = new Finder.Builder(context).build();
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        }

        // try context with image1, image2, resultImage, resultSourceImage, matchSimilarity fields only
        try {
            MatchContext context = new MatchContext.Builder().
                image1(imagesContainer.getDiffImage1File()).
                image2(imagesContainer.getDiffImage2File()).
                resultImage(imagesContainer.getResultImageFile()).
                resultSourceImage(imagesContainer.getResultSourceImageFile()).
                matchSimilarity(0.9999D).
                build();
            Finder finder = new Finder.Builder(context).build();
            Assert.fail("NullPointerException expected");
        } catch (NullPointerException e) {
        }

        // try context with image1, image2, resultImage, resultSourceImage, matchSimilarity fields only
        try {
            MatchContext context = new MatchContext.Builder().
                image1(imagesContainer.getDiffImage1File()).
                image2(imagesContainer.getDiffImage2File()).
                resultImage(imagesContainer.getResultImageFile()).
                resultSourceImage(imagesContainer.getResultSourceImageFile()).
                matchSimilarity(0.0001D).
                build();
            Finder finder = new Finder.Builder(context).build();
            Assert.fail("NullPointerException expected");
        } catch (NullPointerException e) {
        }

        // try context with image1, image2, resultImage, resultSourceImage, matchSimilarity and matchMethod fields only
        try {
            MatchContext context = new MatchContext.Builder().
                image1(imagesContainer.getDiffImage1File()).
                image2(imagesContainer.getDiffImage2File()).
                resultImage(imagesContainer.getResultImageFile()).
                resultSourceImage(imagesContainer.getResultSourceImageFile()).
                matchSimilarity(0.5D).
                matchMethod(TemplateMatchMethod.CV_TM_CCORR_NORMED).
                build();
            Finder finder = new Finder.Builder(context).build();
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        }

        // try context with image1, image2, resultImage, resultSourceImage, matchSimilarity, 
        // matchMethod and negative limit fields only
        try {
            MatchContext context = new MatchContext.Builder().
                image1(imagesContainer.getDiffImage1File()).
                image2(imagesContainer.getDiffImage2File()).
                resultImage(imagesContainer.getResultImageFile()).
                resultSourceImage(imagesContainer.getResultSourceImageFile()).
                matchSimilarity(0.5D).
                matchMethod(TemplateMatchMethod.CV_TM_CCORR_NORMED).
                limit(-1).
                build();
            Finder finder = new Finder.Builder(context).build();
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        }

        // try context with image1, image2, resultImage, resultSourceImage, matchSimilarity, 
        // matchMethod, limit fields only
        try {
            MatchContext context = new MatchContext.Builder().
                image1(imagesContainer.getDiffImage1File()).
                image2(imagesContainer.getDiffImage2File()).
                resultImage(imagesContainer.getResultImageFile()).
                resultSourceImage(imagesContainer.getResultSourceImageFile()).
                matchSimilarity(0.5D).
                matchMethod(TemplateMatchMethod.CV_TM_CCORR_NORMED).
                limit(Integer.MAX_VALUE).
                build();
            Finder finder = new Finder.Builder(context).build();
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        }

        // try context with image1 (not exists), image2, resultImage, resultSourceImage, matchSimilarity, 
        // matchMethod, limit fields only
        try {
            MatchContext context = new MatchContext.Builder().
                image1(new File(System.nanoTime() + ".tmp")).
                image2(imagesContainer.getDiffImage2File()).
                resultImage(imagesContainer.getResultImageFile()).
                resultSourceImage(imagesContainer.getResultSourceImageFile()).
                matchSimilarity(0.5D).
                matchMethod(TemplateMatchMethod.CV_TM_CCORR_NORMED).
                limit(100).
                build();
            Finder finder = new Finder.Builder(context).build();
            Assert.fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
        }

        // try context with image1, image2 (not exists), resultImage, resultSourceImage, matchSimilarity, 
        // matchMethod, limit fields only
        try {
            MatchContext context = new MatchContext.Builder().
                image1(imagesContainer.getDiffImage1File()).
                image2(new File(System.nanoTime() + ".tmp")).
                resultImage(imagesContainer.getResultImageFile()).
                resultSourceImage(imagesContainer.getResultSourceImageFile()).
                matchSimilarity(0.5D).
                matchMethod(TemplateMatchMethod.CV_TM_CCORR_NORMED).
                limit(100).
                build();
            Finder finder = new Finder.Builder(context).build();
            Assert.fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
        }

        // try context with image1, image2, resultImage, resultSourceImage, matchSimilarity, 
        // matchMethod, limit fields only
        try {
            MatchContext context = new MatchContext.Builder().
                image1(imagesContainer.getDiffImage1File()).
                image2(imagesContainer.getDiffImage2File()).
                resultImage(imagesContainer.getResultImageFile()).
                resultSourceImage(imagesContainer.getResultSourceImageFile()).
                matchSimilarity(0.5D).
                matchMethod(TemplateMatchMethod.CV_TM_CCORR_NORMED).
                limit(100).
                build();
            Finder finder = new Finder.Builder(context).build();
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        }

        // try context with image1, image2, resultImage, resultSourceImage, matchSimilarity, 
        // matchMethod, limit and find with diff fields only
        try {
            MatchContext context = new MatchContext.Builder().
                find().
                diff().
                image1(imagesContainer.getDiffImage1File()).
                image2(imagesContainer.getDiffImage2File()).
                resultImage(imagesContainer.getResultImageFile()).
                resultSourceImage(imagesContainer.getResultSourceImageFile()).
                matchSimilarity(0.5D).
                matchMethod(TemplateMatchMethod.CV_TM_CCORR_NORMED).
                limit(100).
                build();
            Finder finder = new Finder.Builder(context).build();
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        }

        MatchContext context;
        Finder finder;
        // try context with image1, image2, resultImage, resultSourceImage, matchSimilarity, 
        // matchMethod, limit and find fields only
        context = new MatchContext.Builder().
            find().
            image1(imagesContainer.getDiffImage1File()).
            image2(imagesContainer.getDiffImage2File()).
            resultImage(imagesContainer.getResultImageFile()).
            resultSourceImage(imagesContainer.getResultSourceImageFile()).
            matchSimilarity(0.5D).
            matchMethod(TemplateMatchMethod.CV_TM_CCORR_NORMED).
            limit(100).
            build();
        finder = new Finder.Builder(context).build();

        // try context with image1, image2, resultImage, resultSourceImage, matchSimilarity, 
        // matchMethod, limit and diff fields only
        context = new MatchContext.Builder().
            diff().
            image1(imagesContainer.getDiffImage1File()).
            image2(imagesContainer.getDiffImage2File()).
            resultImage(imagesContainer.getResultImageFile()).
            resultSourceImage(imagesContainer.getResultSourceImageFile()).
            matchSimilarity(0.5D).
            matchMethod(TemplateMatchMethod.CV_TM_CCORR_NORMED).
            limit(100).
            build();
        finder = new Finder.Builder(context).build();
    }
}
