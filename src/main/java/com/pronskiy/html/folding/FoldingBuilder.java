package com.pronskiy.html.folding;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.SyntaxTraverser;
//import com.jetbrains.php.lang.psi.PhpFile;
//import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.intellij.psi.html.HtmlTag;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class FoldingBuilder extends FoldingBuilderEx implements DumbAware {
    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        FoldingGroup group = FoldingGroup.newGroup("simple");

        List<FoldingDescriptor> descriptors = new ArrayList<>();
        Collection<HtmlTag> tagCollection = PsiTreeUtil.findChildrenOfType(root, HtmlTag.class);

        for (final HtmlTag htmlTag : tagCollection) {

            if (htmlTag.getName().equals("a")) {

                XmlAttribute href = htmlTag.getAttribute("href");
                String text = htmlTag.getValue().getText();

                descriptors.add(new FoldingDescriptor(htmlTag.getNode(),
                        new TextRange(htmlTag.getTextRange().getStartOffset() + 3, // `<a `
                                htmlTag.getTextRange().getEndOffset() - 1),
                        group) {
                    @Override
                    public String getPlaceholderText() {
                        return text;
                    }
                });
            }
            // Новости и релизы
            else if (htmlTag.getText().equals("<img src=\"https://habrastorage.org/files/f57/4e9/991/f574e9991c42407b8fa789a66b854f8d.png\" width=\"20\" />")) {
                descriptors.add(getFoldingDescriptor(group, htmlTag, "[news]")); //"⚡️"
            }
            // Internals
            else if (htmlTag.getText().equals("<img src=\"https://habrastorage.org/files/c9e/e78/803/c9ee7880391644e4aef6cc28ca681ab0.png\" width=\"20\" />")) {
                descriptors.add(getFoldingDescriptor(group, htmlTag, "[elph]")); //"🐘️"
            }
            // Инструменты
            else if (htmlTag.getText().equals("<img src=\"https://habrastorage.org/files/cf8/62d/907/cf862d9072784d04b621238ec137f2ca.png\" width=\"20\" />")) {
                descriptors.add(getFoldingDescriptor(group, htmlTag, "[tool]"));
            }
            // Symfony
            else if (htmlTag.getText().equals("<img src=\"https://habrastorage.org/web/96b/65e/921/96b65e92179a40f2bd1884549973ddd5.png\" width=\"16\" />")) {
                descriptors.add(getFoldingDescriptor(group, htmlTag, "[S]"));
            }
            // Laravel
            else if (htmlTag.getText().equals("<img src=\"https://habrastorage.org/web/314/bd0/f0d/314bd0f0dfc54e3fa7f0c0daef1a2d25.png\" width=\"16\" />")) {
                descriptors.add(getFoldingDescriptor(group, htmlTag, "[L]"));
            }
            // Yii
            else if (htmlTag.getText().equals("<img src=\"https://habrastorage.org/web/569/de4/298/569de4298b764a33894aa517e74324f9.png\" width=\"16\" />")) {
                descriptors.add(getFoldingDescriptor(group, htmlTag, "[Y]"));
            }
            // Async
            else if (htmlTag.getText().equals("<img src=\"https://habrastorage.org/webt/73/2r/pq/732rpqll-yvu0hrtaw6yeism-de.png\" width=\"20\"/>")) {
                descriptors.add(getFoldingDescriptor(group, htmlTag, "[asnc]"));
            }
            // Аудио/Видео
            else if (htmlTag.getText().equals("<img src=\"https://habrastorage.org/files/8dc/c23/677/8dcc236774104f8286a40ceaea553820.png\" width=\"20\">")) {
                descriptors.add(getFoldingDescriptor(group, htmlTag, "[A/V]"));
            }
            // ✅
            else if (htmlTag.getText().equals("<img alt=\"check\" src=\"https://habrastorage.org/webt/qn/pg/4d/qnpg4d87ngmevnvwdg9vjxxa5i4.png\" width=\"12\"/>")) {
                descriptors.add(getFoldingDescriptor(group, htmlTag, "[+]"));
            }
            // Video
            else if (htmlTag.getText().equals("<img alt=\"video\" src=\"https://habrastorage.org/webt/qz/or/43/qzor43-o-hxqow_k8lsqlmniblm.png\" width=\"14\">")) {
                descriptors.add(getFoldingDescriptor(group, htmlTag, "[v]"));
            }
            // Habr
            else if (htmlTag.getText().equals("<img alt=\"habr\" src=\"https://habrastorage.org/webt/-u/wa/el/-uwael5b7b4s2zu-db-7p8iytxg.png\" width=\"12\"/>")) {
                descriptors.add(getFoldingDescriptor(group, htmlTag, "[h]️"));
            }
        }

        return descriptors.toArray(new FoldingDescriptor[0]);
    }

    @NotNull
    private FoldingDescriptor getFoldingDescriptor(FoldingGroup group, HtmlTag htmlTag, String text) {
        return new FoldingDescriptor(htmlTag.getNode(),
                new TextRange(htmlTag.getTextRange().getStartOffset(),
                        htmlTag.getTextRange().getEndOffset()),
                group) {
            @Override
            public String getPlaceholderText() {
                return text;
            }
        };
    }

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull ASTNode node) {
        return "...";
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return true;
    }

}
